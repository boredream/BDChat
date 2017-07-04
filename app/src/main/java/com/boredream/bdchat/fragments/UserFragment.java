package com.boredream.bdchat.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends BaseFragment implements AdapterView.OnItemClickListener {

	private View view;
	private TitleBarView title;
	private RecyclerView rv_user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_user, null);

		initView();

		return view;
	}

	private void initView() {
		title = (TitleBarView) view.findViewById(R.id.title);
		title.setTitleText("我的");
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
}
