package com.boredream.bdchat.presenter;

import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.RxComposer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.rong.imlib.RongIMClient;

public class ChooseContactPresenter implements ChooseContactContract.Presenter {

    private final ChooseContactContract.View view;

    public ChooseContactPresenter(ChooseContactContract.View view) {
        this.view = view;
    }

    @Override
    public void createDiscussion(List<User> users) {
        if(users.size() <= 2) {
            view.showTip("创建人数不能少于3个");
            return;
        }

        // 群聊名称过20个字后不再添加
        int nameMaxSize = 20;
        StringBuilder sbGroupName = new StringBuilder();
        final List<String> userIds = new ArrayList<>();
        for (User user : users) {
            if(sbGroupName.length() < nameMaxSize) {
                sbGroupName.append(user.getNickname()).append("、");
            }

            userIds.add(user.getObjectId());
        }

        final String groupName = sbGroupName.substring(0, sbGroupName.length()-1);
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                        RongIMClient.getInstance().createDiscussion(groupName, userIds,
                                new RongIMClient.CreateDiscussionCallback() {
                                    @Override
                                    public void onSuccess(String s) {
                                        e.onNext(s);
                                        e.onComplete();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        e.onError(new Exception(errorCode.getMessage()));
                                    }
                                });
                    }
                })
                .compose(RxComposer.<String>schedulers())
                .subscribe(new DefaultDisposableObserver<String>(view) {
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);

                        view.createDiscussionSuccess(groupName, s);
                    }
                });
    }
}
