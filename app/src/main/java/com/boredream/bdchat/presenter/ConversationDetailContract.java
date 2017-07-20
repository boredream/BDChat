package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Discussion;

public interface ConversationDetailContract {

    interface View extends BaseView {

        void getUserInfoSuccess(ArrayList<User> users);

        void addMemberSuccess(ArrayList<User> users);

        void removeMemberSuccess(User user);

        void getDiscussionSuccess(Discussion discussion);
    }

    interface Presenter extends BasePresenter {

        void getUserInfo(List<String> userIds);

        void getMemberList(boolean isGroup, String targetId);

        void addMember(Discussion discussion, ArrayList<User> chooseUsers);

        void removeMember(Discussion discussion, ArrayList<User> chooseUsers);
    }
}
