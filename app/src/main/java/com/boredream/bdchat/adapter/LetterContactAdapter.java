package com.boredream.bdchat.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.activity.NewContactActivity;
import com.boredream.bdchat.activity.UserDetailActivity;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成员列表 Adapter
 */
public class LetterContactAdapter extends BaseAdapter {

    private BaseActivity context;
    private List<User> users = new ArrayList<>();
    private Map<String, Integer> indexMap = new HashMap<>();

    public void setUsers(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                return lhs.getLetter().toUpperCase().compareTo(rhs.getLetter().toUpperCase());
            }
        });
        for (int index = 0; index < users.size(); index++) {
            User user = users.get(index);
            String firstLetter = StringUtils.getFirstLetter(user.getLetter());
            if (!indexMap.containsKey(firstLetter)) {
                // 多一个新的朋友
                indexMap.put(firstLetter, index + 1);
            }
        }
        this.users = users;
    }

    public LetterContactAdapter(BaseActivity context) {
        this.context = context;
    }

    public int getPositionByLetter(String firstLetter) {
        Integer position = indexMap.get(firstLetter);
        return position == null ? -1 : position;
    }

    @Override
    public int getCount() {
        return 1 + users.size();
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
            convertView = View.inflate(context, R.layout.item_letter_contact, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == 0) {
            // 新的朋友
            holder.tv_first_letter.setVisibility(View.GONE);
            holder.iv_image.setImageResource(R.drawable.default_image);
            holder.tv_name.setText("新的朋友");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, NewContactActivity.class));
                }
            });
        } else {
            final User user = getItem(position - 1);
            holder.bindData(user);

            String firstLetter = StringUtils.getFirstLetter(user.getLetter());
            if (position == getPositionByLetter(firstLetter)) {
                holder.tv_first_letter.setVisibility(View.VISIBLE);
                holder.tv_first_letter.setText(String.valueOf(firstLetter));
            } else {
                holder.tv_first_letter.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDetailActivity.start(context, user.getObjectId());
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder extends UserViewHolder{
        public TextView tv_first_letter;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_first_letter = (TextView) itemView.findViewById(R.id.tv_first_letter);
        }
    }

}