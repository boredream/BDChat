package com.boredream.bdchat.presenter;

import android.util.Log;

import com.boredream.bdcodehelper.entity.CloudResponse;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;

import java.util.HashMap;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    private final UserDetailContract.View view;

    public UserDetailPresenter(UserDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void loadUserDetail(String userId) {
        HttpRequest.getSingleton()
                .getApiService()
                .getUserById(userId)
                .compose(RxComposer.<User>schedulers())
                .subscribe(new DefaultDisposableObserver<User>(view) {
                    @Override
                    public void onNext(User user) {
                        super.onNext(user);

                        view.loadUserDetailSuccess(user);
                    }
                });
    }

    @Override
    public void friendRequest(String userId) {
        HashMap<String, String> request = new HashMap<>();
        request.put("userId", userId);
        HttpRequest.getSingleton()
                .getApiService()
                .friendRequest(request)
                .compose(RxComposer.<CloudResponse<User>>schedulers())
                .compose(RxComposer.<User>handleCloudResponse())
                .subscribe(new DefaultDisposableObserver<User>(view){
                    @Override
                    public void onNext(User user) {
                        super.onNext(user);

                        view.friendRequestSuccess(user);
                    }
                });
    }
}
