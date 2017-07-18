package com.boredream.bdchat.presenter;

import com.boredream.bdcodehelper.base.LeanCloudObject;
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
    public void applyFriendRequest(final User user) {
        Map<String, String> request = new HashMap<>();
        request.put("userId", user.getObjectId());
        HttpRequest.getSingleton()
                .getApiService()
                .applyFriendRequest(request)
                .compose(RxComposer.<CloudResponse<LeanCloudObject>>schedulers())
                .compose(RxComposer.<LeanCloudObject>handleCloudResponse())
                .subscribe(new DefaultDisposableObserver<LeanCloudObject>(view){
                    @Override
                    public void onNext(LeanCloudObject o) {
                        super.onNext(o);

                        view.applyFriendRequestSuccess(user);
                    }
                });
    }
}
