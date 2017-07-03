package com.boredream.bdchat.presenter;

import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.ErrorConstants;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
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
//        .delay(10, TimeUnit.SECONDS)

        final Observable<User> observable = HttpRequest.getSingleton().loginByTokenWithIm(sessionToken);
        decObservable(observable
                .map(new Function<User, User>() {
                    @Override
                    public User apply(@NonNull User user) throws Exception {
                        return null;
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

        // TODO: 2017/6/30 callback - > rxJava
        decObservable(HttpRequest.getSingleton().loginWithIm(username, password));
    }

    private void decObservable(Observable<User> observable) {
        ObservableDecorator.decorate(observable).subscribe(new DisposableObserver<User>() {

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

                if(!TextUtils.isEmpty(user.getImToken())) {
                    view.loginSuccess(user);
                } else {
                    imLogin(user);
                }
            }
        });
    }

    private void imLogin(final User user) {
        RongIM.connect(user.getImToken(), new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查
             * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {

            }

            /**
             * 连接融云成功
             *
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                if (!view.isActive()) {
                    return;
                }

                // 获取全部好友 // TODO: 2017/6/20
//                IMUserProvider.getInstance().syncAllContacts();

                view.loginSuccess(user);
            }

            /**
             * 连接融云失败
             *
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if (!view.isActive()) {
                    return;
                }

                view.loginError(errorCode.toString());
            }
        });
    }
}
