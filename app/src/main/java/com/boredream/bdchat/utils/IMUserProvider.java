package com.boredream.bdchat.utils;

import android.net.Uri;

import com.boredream.bdchat.entity.event.GetContactsCompleteEvent;
import com.boredream.bdcodehelper.entity.CloudResponse;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;
import com.boredream.bdcodehelper.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
        User user = null;
        try {
            user = HttpRequest.getSingleton()
                    .getApiService()
                    .getUserById(userId)
                    .blockingSingle();
        } catch (Exception e) {
            // do nothing
        }
        return user2contact(user);
    }

    public static List<User> allContacts = new ArrayList<>();

    /**
     * 同步全部好友
     */
    public static void syncAllContacts() {
        // 服务器获取全部好友更新到缓存和数据库中
        HttpRequest.getSingleton()
                .getApiService()
                .getFriends()
                .compose(RxComposer.<CloudResponse<ArrayList<User>>>schedulers())
                .subscribe(new DisposableObserver<CloudResponse<ArrayList<User>>>() {

                    @Override
                    public void onNext(@NonNull CloudResponse<ArrayList<User>> response) {
                        ArrayList<User> results = response.getResult();
                        if (results == null) {
                            return;
                        }

                        StringBuilder sbNames = new StringBuilder();
                        for (User user : results) {
                            // 保存到缓存中
                            if(!allContacts.contains(user)) {
                                allContacts.add(user);
                            }
                            UserInfo userInfo = user2contact(user);
                            sbNames.append(userInfo.getName()).append(", ");
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                        }

                        LogUtils.showLog("syncAllContacts:" + sbNames);
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

    public static UserInfo user2contact(User user) {
        if (user == null) return null;
        String avatarUrl = user.getAvatarUrl() == null ? "" : user.getAvatarUrl();
        return new UserInfo(user.getObjectId(),
                    user.getNickname(),
                    Uri.parse(avatarUrl));
    }

    public static User contact2user(UserInfo contact) {
        if (contact == null) return null;
        User user = new User();
        user.setObjectId(contact.getUserId());
        user.setNickname(contact.getName());
        if(contact.getPortraitUri() != null) {
            user.setAvatarUrl(contact.getPortraitUri().toString());
        }
        return user;
    }

}
