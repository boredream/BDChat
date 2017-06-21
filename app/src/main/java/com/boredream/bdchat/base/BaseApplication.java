package com.boredream.bdchat.base;

import android.app.Application;

import io.rong.imkit.RongIM;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RongIM.init(this);
    }
}
