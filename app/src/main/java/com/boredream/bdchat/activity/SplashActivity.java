package com.boredream.bdchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.LoginContract;
import com.boredream.bdchat.presenter.LoginPresenter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.utils.LogUtils;

public class SplashActivity extends BaseActivity implements LoginContract.View {

    private LoginPresenter loginPresenter;

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
    }

    @Override
    public void loginSuccess(User user) {
        LogUtils.showLog("loginSuccess");
        intent2Activity(MainActivity.class);
    }

    @Override
    public void loginError(String error) {
        LogUtils.showLog("loginError " + error);
        intent2Activity(LoginActivity.class);
    }

}
