package com.example.userinfodemo.presenter;


import android.text.TextUtils;

import com.example.userinfodemo.base.BasePresenter;
import com.example.userinfodemo.contract.IUserInfoContract;
import com.example.userinfodemo.db.UserInfoDbManager;
import com.example.userinfodemo.bean.UserInfo;
import com.example.userinfodemo.activity.UserInfoActivity;
import com.example.userinfodemo.net.UserInfoNet;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
        //采用concat操作符，先读取缓存，再网络请求
        Observable.concat(UserInfoDbManager.getInstance().queryUserInfoByLogin(userName),
                UserInfoNet.getInstance().queryUserInfo(userName))
                .observeOn(AndroidSchedulers.mainThread(), true)
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
