package com.boredream.bdchat.utils;

import android.net.Uri;

import com.boredream.bdchat.entity.GetContactsCompleteEvent;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 实现自定义用户体系
 */
public class IMUserProvider implements RongIM.UserInfoProvider {

    @Override
    public UserInfo getUserInfo(String userId) {
        User user = HttpRequest.getSingleton()
                .getApiService()
                .getUserById(userId)
                .blockingSingle();
        return user2contact(user);
    }

    /**
     * 同步全部好友
     */
    public static void syncAllContacts() {
        // 服务器获取全部好友更新到缓存和数据库中

        // TODO: 2017/7/4 get my friends

        // TODO: 2017/7/4 服务端权限记得改回来，user的find

        HttpRequest.getSingleton()
                .getUsersByUsername(null)
                .compose(RxComposer.<ListResponse<User>>schedulers())
                .subscribe(new DisposableObserver<ListResponse<User>>() {
                    @Override
                    public void onNext(@NonNull ListResponse<User> response) {
                        for (User user : response.getResults()) {
                            // 保存到缓存中
                            UserInfo userInfo = user2contact(user);
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                        }

                        EventBus.getDefault().post(new GetContactsCompleteEvent(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        EventBus.getDefault().post(new GetContactsCompleteEvent(false));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static UserInfo user2contact(User user) {
        String avatarUrl = user.getAvatarUrl() == null ? "" : user.getAvatarUrl();
        return new UserInfo(user.getObjectId(),
                    user.getNickname(),
                    Uri.parse(avatarUrl));
    }

}
