package com.boredream.bdchat.base;

import android.app.Application;

import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.base.AppKeeper;

import io.rong.imkit.RongIM;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppKeeper.init(this);
        RongIM.init(this);
        RongIM.setUserInfoProvider(new IMUserProvider(), true);
    }
}
