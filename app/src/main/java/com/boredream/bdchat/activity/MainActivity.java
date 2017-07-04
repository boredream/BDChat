package com.boredream.bdchat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdchat.R;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.fragments.HomeFragment;
import com.boredream.bdchat.fragments.UserFragment;
import com.boredream.bdcodehelper.fragment.FragmentController;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private RadioGroup rg_bottom_tab;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new UserFragment());
        controller = new FragmentController(this, R.id.fl_content, fragments);

        rg_bottom_tab = (RadioGroup) findViewById(R.id.rg_bottom_tab);
        controller.setRadioGroup(rg_bottom_tab);
        ((RadioButton)findViewById(R.id.rb1)).setChecked(true);
    }
}
