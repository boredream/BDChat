package com.boredream.bdchat.presenter;

import android.os.SystemClock;

import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.ErrorConstants;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;
import com.boredream.bdcodehelper.utils.LogUtils;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

// TODO: 2017/6/30 如何把业务功能，比如这里的登陆，封装到model里，在特殊情况下（还要im登陆）如何处理
public class LoginPresenter implements LoginContract.Presenter {

    private static final int SPLASH_DUR_MIN_TIME = 1500;
    private static final int SPLASH_DUR_MAX_TIME = 5000;

    private final LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void autoLogin(String sessionToken) {
        final Observable<User> observable = HttpRequest.getSingleton().loginByTokenWithIm(sessionToken);
        final long startTime = SystemClock.elapsedRealtime();
        decObservable(observable.flatMap(new Function<User, ObservableSource<User>>() {
                @Override
                public ObservableSource<User> apply(@NonNull User user) throws Exception {
                    // 接口返回后，凑够最短时间跳转
                    long requestTime = SystemClock.elapsedRealtime() - startTime;
                    long delayTime = requestTime < SPLASH_DUR_MIN_TIME
                            ? SPLASH_DUR_MIN_TIME - requestTime : 0;
                    return Observable.just(user).delay(delayTime, TimeUnit.MILLISECONDS);
                }
            })
            .timeout(SPLASH_DUR_MAX_TIME, TimeUnit.MILLISECONDS));
    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            view.showTip("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            view.showTip("密码不能为空");
            return;
        }

        decObservable(HttpRequest.getSingleton().loginWithIm(username, password));
    }

    private void decObservable(Observable<User> observable) {
        observable.compose(RxComposer.<User>schedulers())
                .flatMap(new Function<User, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@NonNull User user) throws Exception {
                        return getImLoginObservable(user);
                    }
                })
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onError(Throwable e) {
                        if (!view.isActive()) {
                            return;
                        }

                        view.loginError(ErrorConstants.parseHttpErrorInfo(e));
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(User user) {
                        if (!view.isActive()) {
                            return;
                        }

                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.getInstance().setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.getInstance().saveSessionToken(user.getSessionToken());
                        // 同步通讯录
                        IMUserProvider.syncAllContacts();

                        view.loginSuccess(user);
                    }
                });
    }

    private Observable<User> getImLoginObservable(final User user) {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<User> e) throws Exception {
                // 融云回调转换成RxJava回调
                RongIM.connect(user.getImToken(), new RongIMClient.ConnectCallback() {

                    /**
                     * Token 错误。可以从下面两点检查
                     * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                     * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                     */
                    @Override
                    public void onTokenIncorrect() {
                        e.onError(new Throwable("im token error"));
                    }

                    /**
                     * 连接融云成功
                     *
                     * @param userid 当前 token 对应的用户 id
                     */
                    @Override
                    public void onSuccess(String userid) {
                        LogUtils.showLog("getImLoginObservable: get imToken success");
                        e.onNext(user);
                        e.onComplete();
                    }

                    /**
                     * 连接融云失败
                     *
                     * @param errorCode 错误码，可到官网 查看错误码对应的注释
                     */
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        e.onError(new Throwable(errorCode.toString()));
                    }
                });
            }
        });
    }
}
