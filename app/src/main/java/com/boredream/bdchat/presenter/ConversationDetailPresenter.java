package com.boredream.bdchat.presenter;

import com.boredream.bdchat.utils.IMUserProvider;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.ErrorConstants;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;
import com.boredream.bdcodehelper.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.UserInfo;

public class ConversationDetailPresenter implements ConversationDetailContract.Presenter {

    private final ConversationDetailContract.View view;

    public ConversationDetailPresenter(ConversationDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void getMemberList(final boolean isGroup, final String targetId) {
        // TODO: 2017/7/19 不需要loading的咋处理更好

        final Observable<ArrayList<User>> observable;
        if (!isGroup) {
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
                    })
                    .compose(RxComposer.<ArrayList<User>>schedulers());
        } else {
            // TODO: 2017/7/19 先在本地缓存中获取，获取不到的去服务端拉取，拉取成功后再缓存到本地

            observable = Observable.create(new ObservableOnSubscribe<Discussion>() {
                        @Override
                        public void subscribe(@NonNull final ObservableEmitter<Discussion> e) throws Exception {
                            RongIM.getInstance().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() {
                                @Override
                                public void onSuccess(Discussion discussion) {
                                    view.getDiscussionSuccess(discussion);

                                    e.onNext(discussion);
                                    e.onComplete();
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    e.onError(new Exception(errorCode.getMessage()));
                                }
                            });
                        }
                    })
                    .flatMap(new Function<Discussion, ObservableSource<ArrayList<User>>>() {
                        @Override
                        public ObservableSource<ArrayList<User>> apply(@NonNull Discussion discussion) throws Exception {
                            // 讨论组，使用ids去服务端查询全部好友
                            List<String> memberIdList = discussion.getMemberIdList();
                            return HttpRequest.getSingleton()
                                    .getUsersByUsernames(memberIdList)
                                    .compose(RxComposer.<User>handleListResponse())
                                    .compose(RxComposer.<ArrayList<User>>schedulers());
                        }
                    });
        }

        observable.subscribe(new DisposableObserver<ArrayList<User>>() {
            @Override
            public void onNext(ArrayList<User> users) {
                view.getMemberListSuccess(users);
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
    public void addMember(final String discussionId, final ArrayList<User> chooseUsers) {
        if(CollectionUtils.isEmpty(chooseUsers)) {
            return;
        }

        Observable.create(new ObservableOnSubscribe<ArrayList<User>>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<ArrayList<User>> e) throws Exception {
                        List<String> userIds = new ArrayList<>();
                        for (User user : chooseUsers) {
                            userIds.add(user.getObjectId());
                        }

                        RongIM.getInstance().addMemberToDiscussion(discussionId, userIds, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                e.onNext(chooseUsers);
                                e.onComplete();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                e.onError(new Exception(errorCode.getMessage()));
                            }
                        });
                    }
                })
                .subscribe(new DefaultDisposableObserver<ArrayList<User>>(view) {
                    @Override
                    public void onNext(ArrayList<User> users) {
                        super.onNext(users);

                        view.addMemberSuccess(chooseUsers);
                    }
                });
    }

    @Override
    public void removeMember(final String discussionId, final ArrayList<User> chooseUsers) {
        if(CollectionUtils.isEmpty(chooseUsers)) {
            return;
        }

        // FIXME: 2017/7/20 融云的sdk只能一次删除一个人
        final User user = chooseUsers.get(0);

        Observable.create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<User> e) throws Exception {
                        RongIM.getInstance().removeMemberFromDiscussion(discussionId, user.getObjectId(), new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                e.onNext(user);
                                e.onComplete();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                e.onError(new Exception(errorCode.getMessage()));
                            }
                        });
                    }
                })
                .subscribe(new DefaultDisposableObserver<User>(view) {
                    @Override
                    public void onNext(User user) {
                        super.onNext(user);

                        view.removeMemberSuccess(user);
                    }
                });
    }

    @Override
    public void clearMessage(final String discussionId) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Object> e) throws Exception {
                RongIM.getInstance().deleteMessages(Conversation.ConversationType.DISCUSSION,
                        discussionId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                if(aBoolean) {
                                    e.onNext(discussionId);
                                    e.onComplete();
                                } else {
                                    e.onError(new Exception("清除消息失败"));
                                }
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                e.onError(new Exception(errorCode.getMessage()));
                            }
                        });
            }
        }).subscribe(new DefaultDisposableObserver<Object>(view) {
            @Override
            public void onNext(Object o) {
                super.onNext(o);

                view.clearMessageSuccess();
            }
        });
    }

    @Override
    public void quitDiscussion(final String discussionId) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Object> e) throws Exception {
                RongIM.getInstance().quitDiscussion(discussionId, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        e.onNext(discussionId);
                        e.onComplete();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        e.onError(new Exception(errorCode.getMessage()));
                    }
                });
            }
        }).subscribe(new DefaultDisposableObserver<Object>(view) {
            @Override
            public void onNext(Object o) {
                super.onNext(o);

                view.quitDiscussionSuccess();
            }
        });
    }
}
