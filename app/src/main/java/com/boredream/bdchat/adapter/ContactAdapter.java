package com.boredream.bdchat.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 成员列表 Adapter
 */
public class ContactAdapter extends BaseAdapter {

    private BaseActivity context;
    private ArrayList<User> users = new ArrayList<>();
    private Map<Character, Integer> indexMap = new HashMap<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                return lhs.getLetter().compareTo(rhs.getLetter());
            }
        });

//        for (int index = 0; index < users.size(); index++) {
//            User user = users.get(index);
//            char firstLetter = StringUtils.getFirstLetter(user.getLetter());
//            if (!indexMap.containsKey(firstLetter)) {
//                indexMap.put(firstLetter, index);
//            }
//        }

        this.users = users;
    }

    public ContactAdapter(BaseActivity context, ArrayList<User> users) {
        this.context = context;
        setUsers(users);
    }

    public int getPositionByLetter(char firstLetter) {
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

//        char firstLetter = StringUtils.getFirstLetter(user.getLetter());
//        if (position == getPositionByLetter(firstLetter)) {
//            holder.tv_first_letter.setVisibility(View.VISIBLE);
//            holder.tv_first_letter.setText(firstLetter+"");
//        } else {
//            holder.tv_first_letter.setVisibility(View.GONE);
//        }
//
//        GlideHelper.showAvatar(context, user.getAvatar(), holder.iv_image);
//        holder.tv_name.setText(user.getNickname());
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ContactDetailActivity.class);
//                intent.putExtra("user", user);
//                context.startActivity(intent);
//            }
//        });

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