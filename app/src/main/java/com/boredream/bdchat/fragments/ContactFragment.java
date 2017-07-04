package com.boredream.bdchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.boredream.bdchat.R;
import com.boredream.bdchat.adapter.ContactAdapter;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdcodehelper.entity.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 联系人页面
 */
public class ContactFragment extends BaseFragment {

    private View view;
    private ListView lv;
    private ContactAdapter adapter;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_contact, container, false);
        initView();
        return view;
    }

    private void initView() {
//        new TitleBuilder(view).setTitleText(getString(R.string.tab2))
//                .setLeftText("聊天室")
//                .setLeftOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        intent2Activity(AllChatRoomActivity.class);
//                    }
//                })
//                .setRightText("+")
//                .setRightOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        intent2Activity(AllContactsActivity.class);
//                    }
//                });

        lv = (ListView) view.findViewById(R.id.lv);
        adapter = new ContactAdapter(activity, users);
        lv.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

//    /**
//     * 收到好友更新完成时响应此事件
//     *
//     * @param event
//     */
//    public void onEvent(GetContactsCompleteEvent event) {
//        if (event.isSuccess()) {
//            showLog("GetContactsComplete");
//            refreshMembers();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMembers();
    }

    private void refreshMembers() {
//        // 过滤当前用户，不显示在通讯录中
//        List<User> allUsers = IMUserProvider.getInstance().getAllUsers();
//        Iterator<User> iterator = allUsers.iterator();
//        for (; iterator.hasNext(); ) {
//            User user = iterator.next();
//            if (UserInfoKeeper.getCurrentUser().getObjectId().equals(user.getObjectId())) {
//                iterator.remove();
//                break;
//            }
//        }
//
//        users.clear();
//        users.addAll(allUsers);
//        adapter.notifyDataSetChanged();
    }
}
