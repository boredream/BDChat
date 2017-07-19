package com.boredream.bdchat.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.adapter.ContactAdapter;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.entity.event.ContactChangeEvent;
import com.boredream.bdchat.presenter.NewContactContract;
import com.boredream.bdchat.presenter.NewContactPresenter;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class NewContactActivity extends BaseActivity implements NewContactContract.View, ContactAdapter.OnContactCtrlListener {

    private NewContactPresenter presenter;

    private TitleBarView title;
    private RecyclerView rv;
    private ContactAdapter adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rv);

        initView();
        presenter.getFriendRequests();
    }

    private void initView() {
        presenter = new NewContactPresenter(this);

        title = (TitleBarView) findViewById(R.id.title);
        title.setTitleText("新的朋友");
        title.setLeftBack(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, users, ContactAdapter.TYPE_NEW_FRIEND);
        adapter.setOnContactCtrlListener(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void getFriendRequestsSuccess(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void apply(User user) {
        presenter.applyFriendRequest(user);
    }

    @Override
    public void applyFriendRequestSuccess(User user) {
        for (User u : users) {
            if(u.getObjectId().equals(user.getObjectId())) {
                // TODO: 2017/7/14 2.0 set 已添加 tag
                break;
            }
        }

        showTip("添加好友成功");
        EventBus.getDefault().post(new ContactChangeEvent(ContactChangeEvent.TYPE_ADD, user));
        users.remove(user);
        adapter.notifyDataSetChanged();
    }
}
