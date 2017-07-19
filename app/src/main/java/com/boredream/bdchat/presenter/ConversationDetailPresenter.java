package com.boredream.bdchat.presenter;

import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.ErrorConstants;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.UserInfo;

public class ConversationDetailPresenter implements ConversationDetailContract.Presenter {

    private final ConversationDetailContract.View view;

    public ConversationDetailPresenter(ConversationDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void getUserInfo(List<String> userIds) {

    }

    @Override
    public void getMemberList(final boolean isGroup, final String targetId) {
        // TODO: 2017/7/19 不需要loading的咋处理更好

        final Observable<ArrayList<User>> observable;
        if(!isGroup) {
            // 单聊,tarId就是对方userid
            observable = Observable.create(new ObservableOnSubscribe<ArrayList<User>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<ArrayList<User>> e) throws Exception {
                    UserInfo contact = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    ArrayList<User> users = new ArrayList<>();
                    users.add(IMUserProvider.contact2user(contact));
                    e.onNext(users);
                    e.onComplete();
                }
            });
        } else {
            // TODO: 2017/7/19 先在本地缓存中获取，获取不到的去服务端拉取，拉取成功后再缓存到本地

            // 讨论组，使用ids去服务端查询全部好友
            Discussion discussion = RongUserInfoManager.getInstance().getDiscussionInfo(targetId);
            List<String> memberIdList = discussion.getMemberIdList();
            observable = HttpRequest.getSingleton()
                    .getUsersByUsernames(memberIdList)
                    .compose(RxComposer.<User>handleListResponse());
        }
        observable.compose(RxComposer.<ArrayList<User>>schedulers())
                .subscribe(new DisposableObserver<ArrayList<User>>() {
                    @Override
                    public void onNext(ArrayList<User> users) {
                        view.getUserInfoSuccess(users);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String error = ErrorConstants.parseHttpErrorInfo(e);
                        view.showTip(error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void addMember(ArrayList<User> chooseUsers) {

    }
}
