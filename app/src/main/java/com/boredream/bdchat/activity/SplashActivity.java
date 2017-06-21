package com.boredream.bdchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.boredream.bdchat.R;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.net.ObservableDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseLoginActivity {

    private static final int SPLASH_DUR_MIN_TIME = 1500;
    private static final int SPLASH_DUR_MAX_TIME = 5 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

        autoLogin();
    }

    private void autoLogin() {
        String[] loginData = UserInfoKeeper.getInstance().getLoginData();
        if (loginData == null) {
            // 不自动登录, 延迟2秒后跳转到欢迎页
            delayIntentToActivity(SPLASH_DUR_MIN_TIME, LoginActivity.class);
            return;
        }

        final long starTime = System.currentTimeMillis();
        doImLogin(starTime, loginData);
    }

    /**
     * IM登录
     */
    private void doImLogin(final long starTime, final String[] loginData) {
        // TODO: 2017/6/20  
//        LCChatKit.getInstance().open(loginData[0], new AVIMClientCallback() {
//            @Override
//            public void done(AVIMClient avimClient, AVIMException e) {
//                AVInstallation installation = AVInstallation.getCurrentInstallation();
//
//                if (null != e || installation == null) {
//                    // IM登录失败或推送信息获取失败
//                    calculateDurTime(starTime, LoginActivity.class);
//                } else {
//                    // IM登录成功，继续用户登录
//                    doUserLogin(starTime, loginData, installation.getInstallationId());
//                }
//            }
//        });
    }

    /**
     * 用户登录
     *
     * @param installationId 推送设备id
     */
    private void doUserLogin(final long starTime, String[] loginData, final String installationId) {
        // TODO: 2017/6/20
//        Observable<User> observable = HttpRequest.login(loginData[0], loginData[1]);
//        ObservableDecorator.decorate(observable)
//                // 设置超时时间,如果接口调用耗时超过最大值,也视为登录失败。防止页面停留时间过长
//                .timeout(SPLASH_DUR_MAX_TIME, TimeUnit.MILLISECONDS)
//                .flatMap(new Func1<User, Observable<BaseEntity>>() {
//                    @Override
//                    public Observable<BaseEntity> call(User user) {
//                        // 登录成功，再更新推送id
//                        Map<String, Object> params = new HashMap<>();
//                        params.put("installationId", installationId);
//                        return ObservableDecorator.decorate(
//                                HttpRequest.getApiService().updateUserById(user.getObjectId(), params));
//                    }
//                })
//                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
//
//                    @Override
//                    public void onError(Throwable e) {
//                        calculateDurTime(starTime, LoginActivity.class);
//                    }
//
//                    @Override
//                    public void onNext(BaseEntity baseEntity) {
//                        // 获取全部好友
//                        IMUserProvider.getInstance().syncAllContacts();
//
//                        calculateDurTime(starTime, MainActivity.class);
//                    }
//                });
    }

    /**
     * 计算接口耗时,如果不足最小时间,则继续延迟补足后再跳转。防止页面停留时间过短
     *
     * @param starTime            接口开始调用时间
     * @param targetActivityClass
     */
    private void calculateDurTime(long starTime, Class<? extends Activity> targetActivityClass) {
        long durTime = System.currentTimeMillis() - starTime;
        // 最小时间处理,防止过快的跳转页面, 至少为SPLASH_DUR_MIN_TIME
        long targetMilliseconds = Math.max(durTime, SPLASH_DUR_MIN_TIME);
        delayIntentToActivity(targetMilliseconds, targetActivityClass);
    }

    /**
     * 延迟跳转
     *
     * @param delayMilliseconds   延迟时间
     * @param targetActivityClass 跳转目标页面类,必须要是Activity的子类
     */
    private void delayIntentToActivity(long delayMilliseconds, Class<? extends Activity> targetActivityClass) {
        Observable.just(targetActivityClass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(delayMilliseconds, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Class<? extends Activity>>() {
                    @Override
                    public void accept(@NonNull Class<? extends Activity> aClass) throws Exception {
                        intent2Activity(aClass);
                        finish();
                    }
                });
    }
}
