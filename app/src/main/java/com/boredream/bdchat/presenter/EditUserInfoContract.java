package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;

import java.io.File;

public interface EditUserInfoContract {

    interface View extends BaseView {

        void updateUserInfoSuccess();

    }

    interface Presenter extends BasePresenter {

        void updateUserAvatar(File avatarImgFile);

        void updateNickname(String nickname);

    }
}
