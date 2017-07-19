package com.boredream.bdchat.adapter;

import android.view.View;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员列表 Adapter
 */
public class LetterChooseContactAdapter extends LetterContactAdapter {

    private int mode;
    // 不可点击操作的用户
    private ArrayList<User> unableUsers = new ArrayList<>();
    // 新选中的用户
    private ArrayList<User> chooseUsers = new ArrayList<>();

    public ArrayList<User> getUnableUsers() {
        return unableUsers;
    }

    public ArrayList<User> getChooseUsers() {
        return chooseUsers;
    }

    public void setUnableUsers(List<User> unableUsers) {
        this.unableUsers.clear();
        this.unableUsers.addAll(unableUsers);
        notifyDataSetChanged();
    }

    public LetterChooseContactAdapter(BaseActivity context, int mode) {
        super(context);
        this.mode = mode;
    }

    @Override
    protected void setData(int position, View convertView, final ViewHolder holder) {
        super.setData(position, convertView, holder);

        holder.iv_choose.setVisibility(View.VISIBLE);

        final User user = getItem(position);
        if(unableUsers.contains(user)) {
            // 不可操作的用户
            holder.iv_choose.setImageResource(R.drawable.cb_checked);
        } else {
            if(chooseUsers.contains(user)) {
                holder.iv_choose.setImageResource(R.drawable.cb_checked_fill);
            } else {
                holder.iv_choose.setImageResource(R.drawable.cb_uncheck);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unableUsers.contains(user)) {
                    return;
                }

                if(chooseUsers.contains(user)) {
                    chooseUsers.remove(user);
                } else {
                    chooseUsers.add(user);
                }
                notifyDataSetChanged();
            }
        });
    }
}