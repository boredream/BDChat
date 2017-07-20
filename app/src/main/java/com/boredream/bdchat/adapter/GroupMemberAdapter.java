package com.boredream.bdchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdchat.R;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;

public class GroupMemberAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context context;
    private ArrayList<User> users;
    private boolean isHost;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUsers(ArrayList<User> users) {
        this.users.addAll(users);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public GroupMemberAdapter(Context context, ArrayList<User> users, boolean isHost) {
        this.context = context;
        this.users = users;
        this.isHost = isHost;
    }

    @Override
    public int getItemCount() {
        return users.size() + (isHost ? 2 : 1);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_grid_contact, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if(isHost && position == getItemCount()-1) {
            // 群主的最后一个是删除成员按钮
            holder.iv_image.setImageResource(R.drawable.ic_oval_remove);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onGroupCtrlListener != null) {
                        onGroupCtrlListener.removeMember();
                    }
                }
            });
        } else if((isHost && position == getItemCount()-2)
                || (!isHost && position == getItemCount()-1)) {
            // 群主的倒数第二个，和非群主的最后一个是添加
            holder.iv_image.setImageResource(R.drawable.ic_oval_add);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onGroupCtrlListener != null) {
                        onGroupCtrlListener.addMember();
                    }
                }
            });
        } else {
            final User data = users.get(position);
            holder.bindData(data);
        }
    }

    private OnGroupCtrlListener onGroupCtrlListener;

    public void setOnGroupCtrlListener(OnGroupCtrlListener onGroupCtrlListener) {
        this.onGroupCtrlListener = onGroupCtrlListener;
    }

    public static interface OnGroupCtrlListener {
        void addMember();
        void removeMember();
    }

}
