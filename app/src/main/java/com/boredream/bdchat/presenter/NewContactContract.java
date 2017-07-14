package com.boredream.bdchat.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;

public interface NewContactContract {

    interface View extends BaseView {

        void getFriendRequestsSuccess(ArrayList<User> users);

        void applyFriendRequestSuccess(String userId);

    }

    interface Presenter extends BasePresenter {

        void getFriendRequests();

        void applyFriendRequest(String userId);

    }
}
