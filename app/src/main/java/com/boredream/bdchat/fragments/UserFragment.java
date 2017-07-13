package com.boredream.bdchat.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.base.BoreBaseEntity;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;
import com.boredream.bdcodehelper.view.TitleBarView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class UserFragment extends BaseFragment implements AdapterView.OnItemClickListener {

	private View view;
	private TitleBarView title;
	private ImageView iv_avatar;
	private TextView tv_username;
	private RecyclerView rv_user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_user, null);

		initView();
		initData();

		return view;
	}

	private void initData() {
		final User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
		if (currentUser == null) {
			return;
		}

		Glide.with(getActivity()).load(currentUser.getAvatarUrl());
		tv_username.setText(currentUser.getNickname());

		// FIXME: 2017/7/4
		tv_username.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, Object> map = new HashMap<>();
				final String newNickname = "昵称_" + new Random().nextInt(100);
				map.put("nickname", newNickname);

				HttpRequest.getSingleton()
						.getApiService()
						.updateUserById(currentUser.getObjectId(), map)
						.compose(RxComposer.<BoreBaseEntity>schedulers())
						.subscribe(new DisposableObserver<BoreBaseEntity>() {
							@Override
							public void onNext(@NonNull BoreBaseEntity boreBaseEntity) {
								currentUser.setNickname(newNickname);
								UserInfoKeeper.getInstance().setCurrentUser(currentUser);
								initData();
							}

							@Override
							public void onError(@NonNull Throwable e) {

							}

							@Override
							public void onComplete() {

							}
						});
			}
		});

	}

	private void initView() {
		title = (TitleBarView) view.findViewById(R.id.title);
		title.setTitleText("我的");
		iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
		tv_username = (TextView) view.findViewById(R.id.tv_username);
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
