package com.boredream.bdchat.presenter;

import android.util.Log;

import com.boredream.bdcodehelper.entity.BaseResponse;
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
        HttpRequest.getSingleton()
                .getApiService()
                .friendRequest(new HashMap<String, String>())
                .compose(RxComposer.<BaseResponse<User>>schedulers())
                .subscribe(new DefaultDisposableObserver<BaseResponse<User>>(view){
                    @Override
                    public void onNext(BaseResponse<User> userBaseResponse) {
                        super.onNext(userBaseResponse);

                        Log.i("DDD", "onNext: ~~~~~~~~~~~~~~~~~");
                    }
                });
    }
}
