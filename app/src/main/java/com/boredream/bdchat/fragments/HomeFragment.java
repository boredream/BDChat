package com.boredream.bdchat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseFragment;
import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private View view;
    private TitleBarView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_home, null);

        initView();

        return view;
    }

    private void initView() {
        title = (TitleBarView) view.findViewById(R.id.title);
        title.setTitleText("首页");
    }

}
