package com.boredream.bdchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.adapter.LetterChooseContactAdapter;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.ChooseContactContract;
import com.boredream.bdchat.presenter.ChooseContactPresenter;
import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.PositionBar;
import com.boredream.bdcodehelper.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class ChooseContactActivity extends BaseActivity implements PositionBar.OnPositionChangedListener, ChooseContactContract.View {

    public static final int REQUEST_CODE_CHOOSE_CONTACT = 110;
    public static final int MODE_ADD = 0;
    public static final int MODE_REMOVE = 1;
    public static final int MODE_CREATE = 2;
    private int mode;
    private ArrayList<User> initUsers;

    private ChooseContactPresenter presenter;
    private TitleBarView title;
    private ListView lv;
    private PositionBar pb_letter;
    private LetterChooseContactAdapter adapter;

    public static void start(Activity activity, ArrayList<User> initUsers, int mode) {
        Intent intent = new Intent(activity, ChooseContactActivity.class);
        intent.putExtra("initUsers", initUsers);
        intent.putExtra("mode", mode);
        activity.startActivityForResult(intent, REQUEST_CODE_CHOOSE_CONTACT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_contact);

        initExtra();
        initView();
        initData();
    }

    private void initExtra() {
        mode = getIntent().getIntExtra("mode", MODE_ADD);
        initUsers = (ArrayList<User>) getIntent().getSerializableExtra("initUsers");
    }

    private void initView() {
        presenter = new ChooseContactPresenter(this);

        title = (TitleBarView) findViewById(R.id.title);
        title.setTitleText("发起群聊");
        title.setRightText("确定");
        title.setLeftBack(this);
        title.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == MODE_CREATE) {
                    // 如果是创建群聊，直接跳转会话页面不再返回
                    createDiscussion();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("mode", mode);
                intent.putExtra("chooseUsers", adapter.getChooseUsers());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        lv = (ListView) findViewById(R.id.lv);
        pb_letter = (PositionBar) findViewById(R.id.pb_letter);
        pb_letter.setOnPositionChangedListener(this);
        adapter = new LetterChooseContactAdapter(this);
        lv.setAdapter(adapter);
    }

    private void createDiscussion() {
        List<User> users = new ArrayList<>();
        users.addAll(adapter.getUnableUsers());
        users.addAll(adapter.getChooseUsers());
        presenter.createDiscussion(users);
    }

    @Override
    public void createDiscussionSuccess(String groupName, String conversationId) {
        RongIM.getInstance().startDiscussionChat(this, conversationId, groupName);
    }

    private void initData() {
        // 显示的全部用户
        List<User> users = new ArrayList<>();
        // 不可点击用户
        List<User> unableUsers = new ArrayList<>();

        User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
        if(mode == MODE_REMOVE) {
            // 删减的，只显示初始成员
            users.addAll(initUsers);
            // 当前用户不可删除
            unableUsers.add(currentUser);
        } else {
            // 新增和创建，显示本地全部好友，注意包括自己
            users.add(currentUser);
            users.addAll(IMUserProvider.allContacts);

            // 如果是创建的，初始成员也记得添加自己
            if(mode == MODE_CREATE) {
                unableUsers.add(currentUser);
            }
            unableUsers.addAll(initUsers);
        }

        adapter.setUsers(users);
        adapter.setUnableUsers(unableUsers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPositionSelected(String key) {
        lv.setSelection(adapter.getPositionByLetter(key));
    }

}
