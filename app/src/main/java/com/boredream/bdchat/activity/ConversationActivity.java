package com.boredream.bdchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.view.TitleBarView;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseActivity {

    private static final int REQUEST_CODE_DETAIL = 110;

    private TitleBarView title;
    private ConversationFragment conversation;
    private Conversation.ConversationType conversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation);

        title = (TitleBarView) findViewById(R.id.title);
        title.getTvTitle().setMaxWidth(DisplayUtils.dp2px(this, 180));
        title.getTvTitle().setTextSize(16);
        title.setTitleText(getIntent().getData().getQueryParameter("title"));
        title.setLeftBack(this);

        conversation = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        conversationType = conversation.getConversationType();
        if(conversationType == Conversation.ConversationType.DISCUSSION
                || conversationType == Conversation.ConversationType.PRIVATE) {
            title.setRightText("聊天详情");
            title.setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConversationDetailActivity.start(ConversationActivity.this,
                            conversationType == Conversation.ConversationType.DISCUSSION,
                            conversation.getTargetId(), REQUEST_CODE_DETAIL);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_DETAIL:
                finish();
                break;
        }
    }
}
