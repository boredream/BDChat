package com.boredream.bdchat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.activity.SearchContactActivity;
import com.boredream.bdchat.adapter.LetterContactAdapter;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdchat.entity.event.ContactChangeEvent;
import com.boredream.bdchat.entity.event.GetContactsCompleteEvent;
import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.utils.LogUtils;
import com.boredream.bdcodehelper.view.PositionBar;
import com.boredream.bdcodehelper.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人页面
 */
public class ContactFragment extends BaseFragment implements PositionBar.OnPositionChangedListener {

    private View view;
    private TitleBarView title;
    private ListView lv;
    private PositionBar pb_letter;
    private LetterContactAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_contact, container, false);
        initView();
        return view;
    }

    private void initView() {
        title = (TitleBarView) view.findViewById(R.id.title);
        title.setTitleText("通讯录");
        title.setRightText("添加");
        title.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.intent2Activity(SearchContactActivity.class);
            }
        });
        lv = (ListView) view.findViewById(R.id.lv);
        pb_letter = (PositionBar) view.findViewById(R.id.pb_letter);
        pb_letter.setOnPositionChangedListener(this);
        adapter = new LetterContactAdapter(activity);
        lv.setAdapter(adapter);
    }

    /**
     * 收到获取好友完成时响应此事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getContactComplete(GetContactsCompleteEvent event) {
        if (event.isSuccess()) {
            LogUtils.showLog("GetContactsComplete");
            refreshMembers();
        }
    }

    /**
     * 收到好友更新时响应此事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactChanged(ContactChangeEvent event) {
        LogUtils.showLog("contactChanged " + event.getType() + " : " + event.getUser().getNickname());

        switch (event.getType()) {
            case ContactChangeEvent.TYPE_ADD:
                IMUserProvider.allContacts.add(event.getUser());
                refreshMembers();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMembers();
    }

    private void refreshMembers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(IMUserProvider.allContacts);

        adapter.setUsers(allUsers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPositionSelected(String key) {
        lv.setSelection(adapter.getPositionByLetter(key));
    }
}
