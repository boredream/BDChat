package com.boredream.bdchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.LoginContract;
import com.boredream.bdchat.presenter.LoginPresenter;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitleBarView;


public class LoginActivity extends BaseActivity implements View.OnClickListener , LoginContract.View {

    private TitleBarView title;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_forget_psw;
    private LinearLayout ll_regist;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        loginPresenter = new LoginPresenter(this);

        // FIXME: 2017/7/4 mock
        et_username.setText("18551681236");
        et_password.setText("123456");
    }

    private void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw);
        ll_regist = (LinearLayout) findViewById(R.id.ll_regist);

        btn_login.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        ll_regist.setOnClickListener(this);

        title.setTitleText("登录");
    }

    private void submit() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        showProgress();
        loginPresenter.login(username, password);
    }

    @Override
    public void loginSuccess(User user) {
        dismissProgress();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginError(String error) {
        dismissProgress();
        showTip(error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
            case R.id.tv_forget_psw:
                PhoneValidateStep1Activity.start(this, PhoneValidateStep1Activity.TYPE_FORGET_PSW);
                break;
            case R.id.ll_regist:
                PhoneValidateStep1Activity.start(this, PhoneValidateStep1Activity.TYPE_REGISTER);
                break;
        }
    }

}
