package com.boredream.bdchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.boredream.bdchat.R;
import com.boredream.bdchat.adapter.GroupMemberAdapter;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.ConversationDetailContract;
import com.boredream.bdchat.presenter.ConversationDetailPresenter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.SettingItemView;
import com.boredream.bdcodehelper.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Discussion;

public class ConversationDetailActivity extends BaseActivity implements View.OnClickListener, ConversationDetailContract.View {

    private ConversationDetailPresenter presenter;

    private TitleBarView title;
    private RecyclerView rv_member;
    private SettingItemView setting_clear;
    private Button btn_quit;

    private boolean isGroup;
    private String targetId;
    private boolean isHost;

    private GroupMemberAdapter adapter;

    public static void start(Context context, boolean isGroup, String targetId) {
        Intent intent = new Intent(context, ConversationDetailActivity.class);
        intent.putExtra("isGroup", isGroup);
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation_detail);

        initExtra();
        initView();
        initData();
    }

    private void initExtra() {
        isGroup = getIntent().getBooleanExtra("isGroup", false);
        targetId = getIntent().getStringExtra("targetId");

        if(isGroup) {
            // 获取讨论组创始人判断当前用户是否为群主
            Discussion discussion = RongUserInfoManager.getInstance().getDiscussionInfo(targetId);
            isHost = UserInfoKeeper.getInstance().getCurrentUser().getObjectId().equals(discussion.getCreatorId());
        }
    }

    private void initData() {
        presenter.getMemberList(isGroup, targetId);
    }

    private void initView() {
        presenter = new ConversationDetailPresenter(this);

        title = (TitleBarView) findViewById(R.id.title);
        rv_member = (RecyclerView) findViewById(R.id.rv_member);
        setting_clear = (SettingItemView) findViewById(R.id.setting_clear);
        btn_quit = (Button) findViewById(R.id.btn_quit);

        title.setTitleText("发起群聊");
        title.setLeftBack(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        layoutManager.setAutoMeasureEnabled(true);
        rv_member.setLayoutManager(layoutManager);
        setting_clear.setMidText("清空聊天记录")
                .setOnClickListener(this);
        btn_quit.setOnClickListener(this);
    }

    private void addM1ember() {
        if(targetId == null) {
            // 单聊
            String discussName = "群聊";
            List<String> userList = new ArrayList<>();
            RongIMClient.getInstance().createDiscussion(discussName, userList, new RongIMClient.CreateDiscussionCallback() {
                @Override
                public void onSuccess(String s) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    @Override
    public void getUserInfoSuccess(ArrayList<User> users) {
        adapter = new GroupMemberAdapter(this, users, isHost);
        rv_member.setAdapter(adapter);
        adapter.setOnGroupCtrlListener(new GroupMemberAdapter.OnGroupCtrlListener() {
            @Override
            public void addMember() {
                // 私聊的时候添加视为创建操作
                ChooseContactActivity.start(ConversationDetailActivity.this, adapter.getUsers(),
                        isGroup ? ChooseContactActivity.MODE_ADD : ChooseContactActivity.MODE_CREATE);
            }

            @Override
            public void removeMember() {
                ChooseContactActivity.start(ConversationDetailActivity.this, adapter.getUsers(),
                        ChooseContactActivity.MODE_REMOVE);
            }
        });
    }

    private void addMember(ArrayList<User> chooseUsers) {
        presenter.addMember(chooseUsers);
    }

    @Override
    public void addMemberSuccess() {

    }

    private void removeMember(ArrayList<User> chooseUsers) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_clear:
                // TODO: 2017/7/18
                break;
            case R.id.btn_quit:
                // TODO: 2017/7/18
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case ChooseContactActivity.REQUEST_CODE_CHOOSE_CONTACT:
                int mode = getIntent().getIntExtra("mode", ChooseContactActivity.MODE_ADD);
                ArrayList<User> chooseUsers = (ArrayList<User>) getIntent().getSerializableExtra("chooseUsers");
                if(mode == ChooseContactActivity.MODE_ADD) {
                    addMember(chooseUsers);
                } else {
                    removeMember(chooseUsers);
                }
                break;
        }
    }

}
