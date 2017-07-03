package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(User user);

        void loginError(String error);

    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

        void autoLogin(String sessionToken);

    }
}
