package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

public interface PhoneValidateContract {

    interface View extends BaseView {

        void requestSmsSuccess();

        void registerSuccess(User user);

        void forgetUserSuccess(String newPsw);

    }

    interface Presenter extends BasePresenter {

        void requestSms(String phone);

        void register(String smsCode, String phone, String password);

        void forgetUser(String smsCode, String phone, String newPsw);

    }
}
