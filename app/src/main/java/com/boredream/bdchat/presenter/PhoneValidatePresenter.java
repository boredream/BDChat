package com.boredream.bdchat.presenter;

import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.entity.UserRegisterByMobilePhone;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;

import java.util.HashMap;
import java.util.Map;

public class PhoneValidatePresenter implements PhoneValidateContract.Presenter {

    private final PhoneValidateContract.View view;

    public PhoneValidatePresenter(PhoneValidateContract.View view) {
        this.view = view;
    }


    @Override
    public void requestSms(String phone) {
        // validate
        if (TextUtils.isEmpty(phone)) {
            view.showTip("请输入手机号");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("mobilePhoneNumber", phone);
        HttpRequest.getSingleton()
                .getApiService()
                .requestSmsCode(params)
                .compose(RxComposer.schedulers())
                .subscribe(new DefaultDisposableObserver<Object>(view){
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);

                        view.requestSmsSuccess();
                    }
                });
    }

    @Override
    public void register(String smsCode, String phone, String password) {
        if (validate(smsCode, phone, password)) return;

        UserRegisterByMobilePhone register = new UserRegisterByMobilePhone();
        register.setMobilePhoneNumber(phone);
        register.setSmsCode(smsCode);
        register.setPassword(password);
        // TODO: 2017/7/4 接口是登陆和注册两用的，如何在注册时检测账号已使用？
        HttpRequest.getSingleton()
                .getApiService()
                .usersByMobilePhone(register)
                .compose(RxComposer.<User>schedulers())
                .subscribe(new DefaultDisposableObserver<User>(view){
                    @Override
                    public void onNext(User user) {
                        super.onNext(user);

                        view.registerSuccess(user);
                    }
                });
    }

    @Override
    public void forgetUser(String smsCode, String phone, final String newPsw) {
        if (validate(smsCode, phone, newPsw)) return;

        Map<String, Object> params = new HashMap<>();
        params.put("password", newPsw);
        HttpRequest.getSingleton()
                .getApiService()
                .resetPasswordBySmsCode(smsCode, params).compose(RxComposer.schedulers())
                .subscribe(new DefaultDisposableObserver<Object>(view){
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);

                        view.forgetUserSuccess(newPsw);
                    }
                });
    }

    private boolean validate(String smsCode, String phone, String password) {
        // validate
        if (TextUtils.isEmpty(smsCode)) {
            view.showTip("请输入验证码");
            return true;
        }

        if (TextUtils.isEmpty(phone)) {
            view.showTip("请输入手机号");
            return true;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            view.showTip("请设置登录密码，不少于6位");
            return true;
        }
        return false;
    }
}
