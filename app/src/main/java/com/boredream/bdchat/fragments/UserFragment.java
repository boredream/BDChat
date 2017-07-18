package com.boredream.bdchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.activity.EditUserInfoActivity;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdchat.entity.event.ContactChangeEvent;
import com.boredream.bdchat.view.UserCardView;
import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

	private View view;
	private TitleBarView title;
	private UserCardView usercard;
	private RecyclerView rv_user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_user, null);

		initView();
		initData();

		return view;
	}

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

	/**
	 * 收到好友更新时响应此事件
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void contactChanged(ContactChangeEvent event) {
		final User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
		if (currentUser == null) {
			return;
		}

		if(!event.getUser().getObjectId().equals(currentUser.getObjectId())) {
			return;
		}

		usercard.setUserInfo(currentUser);
	}

	private void initData() {
		final User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
		if (currentUser == null) {
			return;
		}

		usercard.setUserInfo(currentUser);
	}

	private void initView() {
		title = (TitleBarView) view.findViewById(R.id.title);
		title.setTitleText("我的");
		usercard = (UserCardView) view.findViewById(R.id.usercard);
		usercard.setOnClickListener(this);
		rv_user = (RecyclerView) view.findViewById(R.id.rv_user);
		rv_user.setLayoutManager(new LinearLayoutManager(getActivity()));

		List<SettingItem> items = new ArrayList<>();
		items.add(new SettingItem(-1, "退出登陆", null, -1));
		SettingRecyclerAdapter adapter = new SettingRecyclerAdapter(items, this);
		rv_user.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			case 0:
				UserInfoKeeper.getInstance().logout();
				activity.clearIntent2Login();
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.usercard:
				startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
				break;
		}
	}
}
