package com.example.userinfodemo.presenter;

import android.text.TextUtils;

import com.example.userinfodemo.activity.UserFollowListActivity;
import com.example.userinfodemo.base.BasePresenter;
import com.example.userinfodemo.contract.IUserFollowListContract;
import com.example.userinfodemo.bean.UserFollowInfo;
import com.example.userinfodemo.db.UserInfoDbManager;
import com.example.userinfodemo.net.UserInfoNet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class UserFollowListPresenter extends BasePresenter<UserFollowListActivity> implements IUserFollowListContract.IUserFollowListPresenter {

    @Override
    public void start() {

    }

    @Override
    public void queryUserFollowersInfo(String userName, int page, int perPage) {
        if(TextUtils.isEmpty(userName) || page <= 0 || perPage <= 0){
            return;
        }
        Observable.concat(UserInfoDbManager.getInstance().queryUserFollowInfoByLogin(userName, true),
                UserInfoNet.getInstance().queryUserFollowersInfo(userName, page, perPage))
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Observer<List<UserFollowInfo>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NotNull List<UserFollowInfo> userInfo) {
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

    @Override
    public void queryUserFollowingInfo(String userName, int page, int perPage) {
        if(TextUtils.isEmpty(userName) || page <= 0 || perPage <= 0){
            return;
        }
        Observable.concat(UserInfoDbManager.getInstance().queryUserFollowInfoByLogin(userName, false),
                UserInfoNet.getInstance().queryUserFollowingInfo(userName, page, perPage))
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Observer<List<UserFollowInfo>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NotNull List<UserFollowInfo> userInfo) {
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
