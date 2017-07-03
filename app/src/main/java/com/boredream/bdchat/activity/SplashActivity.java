package com.boredream.bdchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.LoginContract;
import com.boredream.bdchat.presenter.LoginPresenter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity implements LoginContract.View {

    private LoginPresenter loginPresenter;
    private long starTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginPresenter = new LoginPresenter(this);

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
        String sessionToken = UserInfoKeeper.getInstance().getSessionToken();
        loginPresenter.autoLogin(sessionToken);
//
//        if (TextUtils.isEmpty(sessionToken)) {
//            // 不自动登录, 延迟2秒后跳转到欢迎页
//            delayIntentToActivity(SPLASH_DUR_MIN_TIME, LoginActivity.class);
//            return;
//        }
//
//        starTime = System.currentTimeMillis();
//        loginPresenter.login(null, null, sessionToken);
    }

    @Override
    public void loginSuccess(User user) {
        Log.i("DDD", "loginSuccess");
//        calculateDurTime(MainActivity.class);
    }

    @Override
    public void loginError(String error) {
        Log.i("DDD", "loginError " + error);
//        calculateDurTime(LoginActivity.class);
    }

//    /**
//     * 计算接口耗时,如果不足最小时间,则继续延迟补足后再跳转。防止页面停留时间过短
//     */
//    private void calculateDurTime(Class<? extends Activity> targetActivityClass) {
//        long durTime = System.currentTimeMillis() - starTime;
//        // 最小时间处理,防止过快的跳转页面, 至少为SPLASH_DUR_MIN_TIME
//        long targetMilliseconds = Math.max(durTime, SPLASH_DUR_MIN_TIME);
//        delayIntentToActivity(targetMilliseconds, targetActivityClass);
//    }
//
//    /**
//     * 延迟跳转
//     *
//     * @param delayMilliseconds   延迟时间
//     * @param targetActivityClass 跳转目标页面类,必须要是Activity的子类
//     */
//    private void delayIntentToActivity(long delayMilliseconds, Class<? extends Activity> targetActivityClass) {
//        Observable.just(targetActivityClass)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .delay(delayMilliseconds, TimeUnit.MILLISECONDS)
//                .timeout(SPLASH_DUR_MAX_TIME, TimeUnit.MILLISECONDS)
//                .subscribe(new Consumer<Class<? extends Activity>>() {
//                    @Override
//                    public void accept(@NonNull Class<? extends Activity> aClass) throws Exception {
//                        intent2Activity(aClass);
//                        finish();
//                    }
//                });
//    }

}
