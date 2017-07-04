package com.boredream.bdchat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.boredream.bdchat.activity.MainActivity;

public class BaseFragment extends Fragment {

    protected MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
    }
}
