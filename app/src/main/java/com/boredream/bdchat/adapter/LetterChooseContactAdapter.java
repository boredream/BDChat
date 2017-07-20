package com.boredream.bdchat.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boredream.bdchat.R;
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
public class LetterChooseContactAdapter extends BaseAdapter {

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
                indexMap.put(firstLetter, index);
            }
        }
        this.users = users;
    }

    public LetterChooseContactAdapter(BaseActivity context) {
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
            convertView = View.inflate(context, R.layout.item_letter_contact, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = getItem(position);
        holder.bindData(user);

        String firstLetter = StringUtils.getFirstLetter(user.getLetter());
        if (position == getPositionByLetter(firstLetter)) {
            holder.tv_first_letter.setVisibility(View.VISIBLE);
            holder.tv_first_letter.setText(String.valueOf(firstLetter));
        } else {
            holder.tv_first_letter.setVisibility(View.GONE);
        }

        holder.iv_choose.setVisibility(View.VISIBLE);

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