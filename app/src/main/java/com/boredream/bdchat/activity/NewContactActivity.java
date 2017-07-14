package com.boredream.bdchat.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.adapter.ContactAdapter;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.NewContactContract;
import com.boredream.bdchat.presenter.NewContactPresenter;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

public class NewContactActivity extends BaseActivity implements NewContactContract.View {

    private NewContactPresenter presenter;

    private TitleBarView titlebar;
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

        titlebar = (TitleBarView) findViewById(R.id.titlebar);
        titlebar.setTitleText("新的朋友");
        titlebar.setLeftBack(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, users);
        rv.setAdapter(adapter);
    }

    @Override
    public void getFriendRequestsSuccess(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void applyFriendRequestSuccess(String userId) {
        for (User user : users) {
            if(user.getObjectId().equals(userId)) {
                // TODO: 2017/7/14 set 已添加 tag
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }
}
