package com.boredream.bdchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.PhoneValidateContract;
import com.boredream.bdchat.presenter.PhoneValidatePresenter;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.utils.DateUtils;
import com.boredream.bdcodehelper.view.TitleBarView;

/**
 * 短信验证页面步骤二,用于注册和忘记密码的验证短信
 */
public class PhoneValidateStep2Activity extends BaseActivity implements View.OnClickListener, PhoneValidateContract.View {

    // 总倒计时60秒
    private static final long TOTAL_TIME = 60 * DateUtils.ONE_SECOND_MILLIONS;
    // 每次减少1秒
    private static final long COUNT_DOWN_INTERVAL = DateUtils.ONE_SECOND_MILLIONS;

    private PhoneValidatePresenter presenter;

    private TitleBarView title;
    private EditText et_verification_code;
    private Button btn_code_info;
    private Button btn_next;

    /**
     * 0-注册 1-忘记密码
     */
    private int type;
    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validate_step2);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", PhoneValidateStep1Activity.TYPE_REGISTER);
        phone = intent.getStringExtra("phone");
        password = intent.getStringExtra("password");
    }

    private void initView() {
        presenter = new PhoneValidatePresenter(this);
        title = (TitleBarView) findViewById(R.id.title);
        title.setTitleText("手机号验证");

        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        btn_code_info = (Button) findViewById(R.id.btn_code_info);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_code_info.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    private void initData() {
        startCountDown();
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        showTip("短信验证码发送成功");

        btn_code_info.setText("60秒");
        btn_code_info.setEnabled(false);

        // 倒计时开始,共60秒,每次减少1秒
        CountDownTimer timer = new CountDownTimer(TOTAL_TIME, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                // 重新获取(60)
                String restTime = (int) (l / COUNT_DOWN_INTERVAL) + "秒";
                btn_code_info.setText(restTime);
            }

            @Override
            public void onFinish() {
                // 倒计时结束,重置按钮
                btn_code_info.setText("重新获取");
                btn_code_info.setEnabled(true);
            }
        };
        timer.start();
    }

    /**
     * 根据类型提交注册或重置密码接口
     */
    private void submit() {
        String code = et_verification_code.getText().toString().trim();

        if (type == PhoneValidateStep1Activity.TYPE_FORGET_PSW) {
            presenter.forgetUser(code, phone, password);
        } else {
            presenter.register(code, phone, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code_info:
                presenter.requestSms(phone);
                break;
            case R.id.btn_next:
                submit();
                break;
        }
    }

    @Override
    public void requestSmsSuccess() {
        // 短信验证码发送成功后,开始倒计时
        startCountDown();
    }

    @Override
    public void registerSuccess(User user) {
        // 注册成功,还是需要重新登陆一下，用于连接im
        clear2Login("注册成功");
    }

    @Override
    public void forgetUserSuccess(String newPsw) {
        clear2Login("密码重置成功");
    }

    private void clear2Login(String msg) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        showTip(msg);
    }
}
