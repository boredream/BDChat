package com.boredream.bdchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.UserDetailContract;
import com.boredream.bdchat.presenter.UserDetailPresenter;
import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.GlideHelper;
import com.boredream.bdcodehelper.view.TitleBarView;

import io.rong.imkit.RongIM;

public class UserDetailActivity extends BaseActivity implements UserDetailContract.View {

    private UserDetailPresenter presenter;
    private TitleBarView titlebar;
    private ImageView iv_avatar;
    private TextView tv_username;
    private String userId;
    private Button btn;

    public static void start(Context context, String userId) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_detail);

        initView();
        initData();
    }

    private void initView() {
        presenter = new UserDetailPresenter(this);

        titlebar = (TitleBarView) findViewById(R.id.titlebar);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        btn = (Button) findViewById(R.id.btn);
    }

    private void initData() {
        userId = getIntent().getStringExtra("userId");
        presenter.loadUserDetail(userId);
    }

    @Override
    public void loadUserDetailSuccess(final User user) {
        GlideHelper.loadImg(iv_avatar, user.getAvatarUrl());
        tv_username.setText(user.getNickname());

        final boolean isFriend = !IMUserProvider.allContacts.contains(user); // FIXME: 2017/7/13 
        btn.setVisibility(View.VISIBLE);
        btn.setText(isFriend ? "发起聊天" : "添加到通讯录");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFriend) {
                    RongIM.getInstance().startPrivateChat(UserDetailActivity.this,
                            user.getObjectId(), user.getNickname());
                } else {
                    presenter.friendRequest(user.getObjectId());
                }
            }
        });
    }
}
