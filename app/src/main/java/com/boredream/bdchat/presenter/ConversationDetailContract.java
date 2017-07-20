package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;

import io.rong.imlib.model.Discussion;

public interface ConversationDetailContract {

    interface View extends BaseView {

        void getMemberListSuccess(ArrayList<User> users);

        void addMemberSuccess(ArrayList<User> users);

        void removeMemberSuccess(User user);

        void getDiscussionSuccess(Discussion discussion);

        void clearMessageSuccess();

        void quitDiscussionSuccess();
    }

    interface Presenter extends BasePresenter {

        void getMemberList(boolean isGroup, String targetId);

        void addMember(String discussionId, ArrayList<User> chooseUsers);

        void removeMember(String discussionId, ArrayList<User> chooseUsers);

        void clearMessage(String discussionId);

        void quitDiscussion(String discussionId);
    }
}
