package com.boredream.bdchat.presenter;

import com.boredream.bdcodehelper.entity.CloudResponse;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewContactPresenter implements NewContactContract.Presenter {

    private final NewContactContract.View view;

    public NewContactPresenter(NewContactContract.View view) {
        this.view = view;
    }

    @Override
    public void getFriendRequests() {
        HttpRequest.getSingleton()
                .getApiService()
                .getFriendRequests()
                .compose(RxComposer.<CloudResponse<ArrayList<User>>>schedulers())
                .compose(RxComposer.<ArrayList<User>>handleCloudResponse())
                .subscribe(new DefaultDisposableObserver<ArrayList<User>>(view) {
                    @Override
                    public void onNext(ArrayList<User> users) {
                        super.onNext(users);

                        view.getFriendRequestsSuccess(users);
                    }
                });
    }

    @Override
    public void applyFriendRequest(final String userId) {
        Map<String, String> request = new HashMap<>();
        request.put("userId", userId);
        HttpRequest.getSingleton()
                .getApiService()
                .applyFriendRequest(request)
                .compose(RxComposer.<CloudResponse<Object>>schedulers())
                .compose(RxComposer.handleCloudResponse())
                .subscribe(new DefaultDisposableObserver<Object>(view){
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);

                        view.applyFriendRequestSuccess(userId);
                    }
                });
    }
}
