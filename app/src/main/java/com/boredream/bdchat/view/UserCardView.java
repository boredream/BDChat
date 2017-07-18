package com.boredream.bdchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.GlideHelper;

public class UserCardView extends FrameLayout {

    private ImageView iv_avatar;
    private TextView tv_username;
    private TextView tv_userid;

    public UserCardView(Context context) {
        super(context);
        init(context);
    }

    public UserCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.include_user_card, this);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
    }

    public void setUserInfo(User user) {
        GlideHelper.loadOvalImg(iv_avatar, user.getAvatarUrl());
        tv_username.setText(user.getNickname());
        tv_userid.setText("手机号：" + user.getUsername());
    }

}
