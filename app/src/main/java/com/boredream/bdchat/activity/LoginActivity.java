package com.boredream.bdchat.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdchat.R;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitlebarView;

import io.reactivex.observers.DisposableObserver;


public class LoginActivity extends BaseLoginActivity implements View.OnClickListener {

    private TitlebarView title;
    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_remember_me;
    private Button btn_login;
    private TextView tv_forget_psw;
    private LinearLayout ll_regist;

    /**
     * 是否为验证登录,true-登录成功后,直接finish返回到来源页 false-登录成功后跳转到主页
     */
    private boolean checkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        checkLogin = getIntent().getBooleanExtra("checkLogin", false);
    }

    private void initView() {
        title = (TitlebarView) findViewById(R.id.title);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_remember_me = (CheckBox) findViewById(R.id.cb_remember_me);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw);
        ll_regist = (LinearLayout) findViewById(R.id.ll_regist);

        btn_login.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        ll_regist.setOnClickListener(this);

        title.setTitleText("登录");
    }

    private void initData() {
        String[] loginData = UserInfoKeeper.getInstance().getLoginData();
        if (loginData != null) {
            cb_remember_me.setChecked(true);
            et_username.setText(loginData[0]);
            et_password.setText(loginData[1]);
        }
    }

    private void submit() {
        // validate
        final String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();
        doLogin(username, password);
    }

    private void doLogin(String username, String password) {
        login(username, password, new DisposableObserver<User>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
            }

            @Override
            public void onNext(User user) {
                dismissProgressDialog();

                // 获取全部好友 // TODO: 2017/6/20
//                IMUserProvider.getInstance().syncAllContacts();

                if (checkLogin) {
                    // 是验证登录,登录成功后,直接finish返回到来源页
                    finish();
                } else {
                    // 不是验证登录,登录成功后跳转到主页
                    intent2Activity(MainActivity.class);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(this, PhoneValidateStep1Activity.class);
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
//            case R.id.tv_forget_psw:
//                intent.putExtra("type", 1);
//                startActivity(intent);
//                break;
//            case R.id.ll_regist:
//                intent.putExtra("type", 0);
//                startActivity(intent);
//                break;
        }
    }
}
