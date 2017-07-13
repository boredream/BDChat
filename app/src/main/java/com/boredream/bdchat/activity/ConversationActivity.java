package com.boredream.bdchat.activity;

import android.os.Bundle;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdcodehelper.view.TitleBarView;

public class ConversationActivity extends BaseActivity {

    private TitleBarView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation);

        title = (TitleBarView) findViewById(R.id.title);
        title.setTitleText(getIntent().getData().getQueryParameter("title"));
        title.setLeftBack(this);
    }
}
