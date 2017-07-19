package com.boredream.bdchat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.EditUserInfoContract;
import com.boredream.bdchat.presenter.EditUserInfoPresenter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitleBarView;

public class EditNicknameActivity extends BaseActivity implements EditUserInfoContract.View {

    private EditUserInfoPresenter presenter;
    private TitleBarView title;
    private EditText et_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_nickname);
        initView();
        initData();
    }

    private void initData() {
        User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
        if(currentUser == null) {
            return;
        }

        et_nickname.setText(currentUser.getNickname());
    }

    private void initView() {
        presenter = new EditUserInfoPresenter(this);
        title = (TitleBarView) findViewById(R.id.title);
        et_nickname = (EditText) findViewById(R.id.et_nickname);

        title.setTitleText("更改名字");
        title.setLeftBack(this);
        title.setRightText("保存");
        title.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = et_nickname.getText().toString().trim();
                presenter.updateNickname(nickname);
            }
        });
    }

    @Override
    public void updateUserInfoSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
