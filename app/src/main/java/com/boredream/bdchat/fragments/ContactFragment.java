package com.boredream.bdchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.activity.NewContactActivity;
import com.boredream.bdchat.adapter.LetterContactAdapter;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdchat.entity.GetContactsCompleteEvent;
import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.utils.LogUtils;
import com.boredream.bdcodehelper.view.PositionBar;
import com.boredream.bdcodehelper.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
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
//                activity.intent2Activity(SearchContactActivity.class);
                // FIXME: 2017/7/14
                activity.intent2Activity(NewContactActivity.class);
            }
        });
        lv = (ListView) view.findViewById(R.id.lv);
        pb_letter = (PositionBar) view.findViewById(R.id.pb_letter);
        pb_letter.setOnPositionChangedListener(this);
        adapter = new LetterContactAdapter(activity);
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

    /**
     * 收到好友更新完成时响应此事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getContactComplete(GetContactsCompleteEvent event) {
        if (event.isSuccess()) {
            LogUtils.showLog("GetContactsComplete");
            refreshMembers();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMembers();
    }

    private void refreshMembers() {
        // 过滤当前用户，不显示在通讯录中
        List<User> allUsers = IMUserProvider.allContacts;

        // FIXME: 2017/7/5 使用我的好友替代全部人员
        Iterator<User> iterator = allUsers.iterator();
        for (; iterator.hasNext(); ) {
            User user = iterator.next();
            if (UserInfoKeeper.getInstance().getCurrentUser().getObjectId().equals(user.getObjectId())) {
                iterator.remove();
                break;
            }
        }

        adapter.setUsers(allUsers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPositionSelected(String key) {
        lv.setSelection(adapter.getPositionByLetter(key));
    }
}
