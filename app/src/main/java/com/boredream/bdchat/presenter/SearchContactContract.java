package com.boredream.bdchat.presenter;

import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.User;

import java.util.ArrayList;

public interface SearchContactContract {

    interface View extends BaseView {

        void searchSuccess(ArrayList<User> users, boolean isLoadMore, boolean haveMore);

        void searchError();

    }

    interface Presenter extends BasePresenter {

        void search(String phone, boolean isLoadMore);

    }
}
