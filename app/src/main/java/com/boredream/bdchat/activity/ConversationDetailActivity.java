package com.boredream.bdchat.activity;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.view.SettingItemView;
import com.boredream.bdcodehelper.view.TitleBarView;

import java.util.ArrayList;

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

    public static void start(Activity context, boolean isGroup, String targetId, int requestCode) {
        Intent intent = new Intent(context, ConversationDetailActivity.class);
        intent.putExtra("isGroup", isGroup);
        intent.putExtra("targetId", targetId);
        context.startActivityForResult(intent, requestCode);
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
        rv_member.setLayoutManager(layoutManager);
        setting_clear.setMidText("清空聊天记录")
                .setOnClickListener(this);
        btn_quit.setOnClickListener(this);
    }

    @Override
    public void getDiscussionSuccess(Discussion discussion) {
        if(discussion == null) return;
        isHost = UserInfoKeeper.getInstance().getCurrentUser().getObjectId().equals(discussion.getCreatorId());
    }

    @Override
    public void getMemberListSuccess(ArrayList<User> users) {
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
        presenter.addMember(targetId, chooseUsers);
    }

    @Override
    public void addMemberSuccess(ArrayList<User> users) {
        showTip("添加成功");
        adapter.addUsers(users);
        adapter.notifyDataSetChanged();
    }

    private void removeMember(ArrayList<User> chooseUsers) {
        presenter.removeMember(targetId, chooseUsers);
    }

    @Override
    public void removeMemberSuccess(User user) {
        showTip("移除成功");
        adapter.removeUser(user);
        adapter.notifyDataSetChanged();
    }

    private void clearMessage() {
        DialogUtils.showCommonDialog(this, "是否确认清空聊天记录？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.clearMessage(targetId);
            }
        });
    }

    @Override
    public void clearMessageSuccess() {
        showTip("消息清除成功");
    }

    @Override
    public void quitDiscussionSuccess() {
        showTip("退出讨论组");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_clear:
                clearMessage();
                break;
            case R.id.btn_quit:
                presenter.quitDiscussion(targetId);
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
                int mode = data.getIntExtra("mode", ChooseContactActivity.MODE_ADD);
                ArrayList<User> chooseUsers = (ArrayList<User>) data.getSerializableExtra("chooseUsers");
                if(mode == ChooseContactActivity.MODE_ADD) {
                    addMember(chooseUsers);
                } else {
                    removeMember(chooseUsers);
                }
                break;
        }
    }

}
