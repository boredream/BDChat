package com.boredream.bdchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.activity.UserDetailActivity;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.GlideHelper;

public class UserViewHolder extends RecyclerView.ViewHolder {
    // TODO: 2017/7/19 button 和 cb 是否在基础信息里？

    public ImageView iv_image;
    public TextView tv_name;
    public ImageView iv_choose;

    public UserViewHolder(final View itemView) {
        super(itemView);
        iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        iv_choose = (ImageView) itemView.findViewById(R.id.iv_choose);
    }
    
    public void bindData(final User user) {
        GlideHelper.loadImg(iv_image, user.getAvatarUrl());
        tv_name.setText(user.getNickname());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.start(itemView.getContext(), user.getObjectId());
            }
        });
    }
}
