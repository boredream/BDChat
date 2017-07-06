package com.boredream.bdchat.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.GlideHelper;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * 成员列表 Adapter
 */
public class ContactAdapter extends BaseAdapter {

    private BaseActivity context;
    private List<User> users = new ArrayList<>();
    private Map<String, Integer> indexMap = new HashMap<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                return lhs.getLetter().compareTo(rhs.getLetter());
            }
        });
        for (int index = 0; index < users.size(); index++) {
            User user = users.get(index);
            String firstLetter = StringUtils.getFirstLetter(user.getLetter());
            if (!indexMap.containsKey(firstLetter)) {
                indexMap.put(firstLetter, index);
            }
        }
        this.users = users;
    }

    public ContactAdapter(BaseActivity context) {
        this.context = context;
    }

    public int getPositionByLetter(String firstLetter) {
        Integer position = indexMap.get(firstLetter);
        return position == null ? -1 : position;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.item_contact, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = getItem(position);

        String firstLetter = StringUtils.getFirstLetter(user.getLetter());
        if (position == getPositionByLetter(firstLetter)) {
            holder.tv_first_letter.setVisibility(View.VISIBLE);
            holder.tv_first_letter.setText(String.valueOf(firstLetter));
        } else {
            holder.tv_first_letter.setVisibility(View.GONE);
        }

        GlideHelper.loadImg(holder.iv_image, user.getAvatarUrl());

        holder.tv_name.setText(user.getNickname());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startPrivateChat(context, user.getObjectId(), user.getNickname());
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_first_letter;
        public ImageView iv_image;
        public TextView tv_name;

        public ViewHolder(final View itemView) {
            tv_first_letter = (TextView) itemView.findViewById(R.id.tv_first_letter);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}