package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface ConversationDetailContract {

    interface View extends BaseView {

        void getUserInfoSuccess(ArrayList<User> users);

        void addMemberSuccess();

    }

    interface Presenter extends BasePresenter {

        void getUserInfo(List<String> userIds);

        void getMemberList(boolean isGroup, String targetId);

        void addMember(ArrayList<User> chooseUsers);
    }
}
