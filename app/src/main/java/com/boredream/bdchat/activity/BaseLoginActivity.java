package com.boredream.bdchat.activity;

import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.BaseHttpRequest;

import io.reactivex.observers.DisposableObserver;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Subscriber;

public class BaseLoginActivity extends BaseActivity {

    /**
     * 业务登陆
     */
    protected void login(final String username, final String password, final DisposableObserver<User> subscriber) {
        BaseHttpRequest.login(username, password)
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        // TODO: 2017/6/20 session
                        imLogin(user.getSessionToken(), new Subscriber<Object>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Object o) {

                            }
                        });
                    }
                });
    }

    /**
     * IM登录
     */
    private void imLogin(String token, final Subscriber<?> subscriber) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                subscriber.onNext(null);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                // IM登录失败或推送信息获取失败
                dismissProgressDialog();
                showToast("登录失败 " + errorCode.toString());
                subscriber.onError(new Throwable(errorCode.toString()));
            }
        });
    }

}
