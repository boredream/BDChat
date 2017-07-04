package com.boredream.bdchat.base;


import android.content.Intent;
import android.os.Bundle;

import com.boredream.bdchat.activity.LoginActivity;
import com.boredream.bdchat.activity.MainActivity;
import com.boredream.bdcodehelper.base.BoreBaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class BaseActivity extends BoreBaseActivity {

    private boolean couldDoubleBackExit;
    private boolean doubleBackExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 2017/7/4

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }
    }

    /**
     * 清空任务栈跳转至登录页
     */
    public void clearIntent2Login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 设置是否可以双击返回退出，需要有该功能的页面set true即可
     *
     * @param couldDoubleBackExit true-开启双击退出
     */
    public void setCouldDoubleBackExit(boolean couldDoubleBackExit) {
        this.couldDoubleBackExit = couldDoubleBackExit;
    }

    @Override
    public void onBackPressed() {
        if (!couldDoubleBackExit) {
            // 非双击退出状态，使用原back逻辑
            super.onBackPressed();
            return;
        }

        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackExitPressedOnce) {
            exit();
            return;
        }

        doubleBackExitPressedOnce = true;
        showTip("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        // 延迟两秒后重置标志位为false
                        doubleBackExitPressedOnce = false;
                    }
                });
    }

    /**
     * 退出程序
     */
    protected void exit() {
        // 退出程序方法有多种
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("exit", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
