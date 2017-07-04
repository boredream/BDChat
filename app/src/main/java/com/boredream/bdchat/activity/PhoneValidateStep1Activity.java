package com.boredream.bdchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.PhoneValidateContract;
import com.boredream.bdchat.presenter.PhoneValidatePresenter;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitleBarView;

/**
 * 短信验证页面步骤一,用于注册和忘记密码的发送短信和信息填写
 */
public class PhoneValidateStep1Activity extends BaseActivity implements View.OnClickListener, PhoneValidateContract.View {

    /**
     * 0-注册
     */
    public static final int TYPE_REGISTER = 0;

    /**
     * 1-忘记密码
     */
    public static final int TYPE_FORGET_PSW = 1;

    /**
     * 0-注册 1-忘记密码
     */
    private int type;

    private EditText et_password;

    private PhoneValidatePresenter presenter;
    private TitleBarView title;
    private EditText et_username;

    private Button btn_next;

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, PhoneValidateStep1Activity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validate_step1);

        initExtras();
        initView();
    }

    private void initExtras() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", TYPE_REGISTER);
    }

    private void initView() {
        presenter = new PhoneValidatePresenter(this);
        title = (TitleBarView) findViewById(R.id.title);
        title.setTitleText(type == TYPE_FORGET_PSW ? "重置密码" : "注册");
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void next() {
        String phone = et_username.getText().toString().trim();
        presenter.requestSms(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                next();
                break;
        }
    }

    @Override
    public void requestSmsSuccess() {
        String phone = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        // 短信验证码发送成功后,跳转到短信验证页,同时传递所需数据
        Intent intent = new Intent(PhoneValidateStep1Activity.this, PhoneValidateStep2Activity.class);
        intent.putExtra("type", type);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    @Override
    public void registerSuccess(User user) {
        // do nothing
    }

    @Override
    public void forgetUserSuccess(String newPsw) {
        // do nothing
    }
}
