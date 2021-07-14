package com.example.userinfodemo.presenter;


import android.text.TextUtils;

import com.example.userinfodemo.base.BasePresenter;
import com.example.userinfodemo.common.RxTransformUtils;
import com.example.userinfodemo.contract.IUserInfoContract;
import com.example.userinfodemo.model.UserInfo;
import com.example.userinfodemo.net.RetrofitManager;
import com.example.userinfodemo.net.UsersService;
import com.example.userinfodemo.activity.UserInfoActivity;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserInfoPresenter extends BasePresenter<UserInfoActivity> implements IUserInfoContract.IUserInfoPresenter {

    @Override
    public void start() {
    }

    @Override
    public void getUserInfo(String userName) {
        if(TextUtils.isEmpty(userName)){
            return;
        }
        RetrofitManager.getInstance().createUsersService().getUserInfo(userName)
                .compose(RxTransformUtils.schedule())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull UserInfo userInfo) {
                        mView.updateData(userInfo);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
