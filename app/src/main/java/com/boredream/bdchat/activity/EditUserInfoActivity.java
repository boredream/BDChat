package com.boredream.bdchat.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.EditUserInfoContract;
import com.boredream.bdchat.presenter.EditUserInfoPresenter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.GlideHelper;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.ImageUtils;
import com.boredream.bdcodehelper.view.TitleBarView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class EditUserInfoActivity extends BaseActivity implements View.OnClickListener, EditUserInfoContract.View {

    private static final int REQUEST_CODE_EDIT_NICKNAME = 110;

    private EditUserInfoPresenter presenter;
    private TitleBarView titlebar;
    private ImageView iv_avatar;
    private LinearLayout ll_avatar;
    private TextView tv_username;
    private LinearLayout ll_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_user_info);
        initView();
        initData();
    }

    private void initData() {
        User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
        if(currentUser == null) {
            return;
        }

        GlideHelper.loadImg(iv_avatar, currentUser.getAvatarUrl());
        tv_username.setText(currentUser.getNickname());
    }

    private void initView() {
        presenter = new EditUserInfoPresenter(this);
        titlebar = (TitleBarView) findViewById(R.id.titlebar);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        ll_avatar = (LinearLayout) findViewById(R.id.ll_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        ll_username = (LinearLayout) findViewById(R.id.ll_username);

        titlebar.setTitleText("个人信息");
        titlebar.setLeftBack(this);
        ll_avatar.setOnClickListener(this);
        ll_username.setOnClickListener(this);
    }

    private void updateAvatar(Uri imageUri) {
        // 先从本地获取图片,利用Glide压缩图片后获取byte[]
        int maxSize = DisplayUtils.dp2px(this, 300);
        showProgress();
        Glide.with(this)
                .asFile()
                .load(imageUri)
                .into(new SimpleTarget<File>(maxSize, maxSize) {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        presenter.updateUserAvatar(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        showTip("获取解析失败");
                        dismissProgress();
                    }
        });
    }

    @Override
    public void updateUserInfoSuccess() {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_avatar:
                ImageUtils.showImagePickDialog(this);
                break;
            case R.id.ll_username:
                startActivityForResult(new Intent(this, EditNicknameActivity.class), REQUEST_CODE_EDIT_NICKNAME);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_EDIT_NICKNAME:
                initData();
                break;
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                updateAvatar(data.getData());
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                updateAvatar(ImageUtils.imageUriFromCamera);
                break;
        }
    }
}
