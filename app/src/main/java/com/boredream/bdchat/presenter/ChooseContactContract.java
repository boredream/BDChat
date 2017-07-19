package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

import java.util.List;

public interface ChooseContactContract {

    interface View extends BaseView {

        void createDiscussionSuccess(String groupName, String conversationId);

    }

    interface Presenter extends BasePresenter {

        void createDiscussion(List<User> users);

    }
}
