package com.boredream.bdchat.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdcodehelper.view.TitleBarView;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class ConvListFragment extends BaseFragment {

    private View view;
    private TitleBarView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_container, null);

        initView();

        return view;
    }

    private void initView() {
        title = (TitleBarView) view.findViewById(R.id.title);
        title.setTitleText("会话");

        initFragment();
    }

    private void initFragment() {
        ConversationListFragment fragment = (ConversationListFragment) activity
                .getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new ConversationListFragment();
            // TODO listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri = Uri.parse("rong://" + activity.getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        // .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                        .build();
            fragment.setUri(uri);

            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, fragment);
            ft.commit();
        }
    }

}
