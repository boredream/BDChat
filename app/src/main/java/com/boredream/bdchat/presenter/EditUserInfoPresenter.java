package com.boredream.bdchat.presenter;

import com.boredream.bdchat.entity.event.ContactChangeEvent;
import com.boredream.bdcodehelper.base.LeanCloudObject;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditUserInfoPresenter implements EditUserInfoContract.Presenter {

    private final EditUserInfoContract.View view;

    public EditUserInfoPresenter(EditUserInfoContract.View view) {
        this.view = view;
    }

    private String newAvatarUrl;

    @Override
    public void updateUserAvatar(File avatarImgFile) {
        final User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
        final String filename = avatarImgFile.getName();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), avatarImgFile);

        final HttpRequest.ApiService apiService = HttpRequest.getSingleton().getApiService();
        apiService.fileUpload(filename, requestBody)
                .flatMap(new Function<FileUploadResponse, ObservableSource<LeanCloudObject>>() {
                    @Override
                    public ObservableSource<LeanCloudObject> apply(@NonNull FileUploadResponse response) throws Exception {
                        // 图片上传成功，修改数据
                        Map<String, Object> updatInfo = new HashMap<>();
                        newAvatarUrl = response.getUrl();
                        updatInfo.put("avatarUrl", newAvatarUrl);
                        return apiService.updateUserById(currentUser.getObjectId(), updatInfo);
                    }
                })
                .compose(RxComposer.<LeanCloudObject>schedulers())
                .subscribe(new DefaultDisposableObserver<LeanCloudObject>(view) {
                    @Override
                    public void onNext(LeanCloudObject leanCloudObject) {
                        super.onNext(leanCloudObject);

                        // 更新用户信息变化
                        currentUser.setAvatarUrl(newAvatarUrl);
                        EventBus.getDefault().post(new ContactChangeEvent(ContactChangeEvent.TYPE_CHANG, currentUser));
                        view.updateUserInfoSuccess();
                    }
                });
    }

    @Override
    public void updateNickname(final String nickname) {
        final User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
        Map<String, Object> updatInfo = new HashMap<>();
        updatInfo.put("nickname", nickname);
        HttpRequest.getSingleton()
                .getApiService()
                .updateUserById(currentUser.getObjectId(), updatInfo)
                .compose(RxComposer.<LeanCloudObject>schedulers())
                .subscribe(new DefaultDisposableObserver<LeanCloudObject>(view) {
                    @Override
                    public void onNext(LeanCloudObject leanCloudObject) {
                        super.onNext(leanCloudObject);

                        // 更新用户信息变化
                        currentUser.setNickname(nickname);
                        EventBus.getDefault().post(new ContactChangeEvent(ContactChangeEvent.TYPE_CHANG, currentUser));
                        view.updateUserInfoSuccess();
                    }
                });
    }
}
