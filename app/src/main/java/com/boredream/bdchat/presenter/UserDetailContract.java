package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

public interface UserDetailContract {

    interface View extends BaseView {

        void loadUserDetailSuccess(User user);

        void friendRequestSuccess(User user);
    }

    interface Presenter extends BasePresenter {

        void loadUserDetail(String userId);

        void friendRequest(String userId);
    }
}
