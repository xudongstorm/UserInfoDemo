package com.example.userinfodemo.presenter;

import android.util.Log;

import com.example.userinfodemo.activity.UserFollowListActivity;
import com.example.userinfodemo.base.BasePresenter;
import com.example.userinfodemo.common.RxTransformUtils;
import com.example.userinfodemo.contract.IUserFollowListContract;
import com.example.userinfodemo.model.UserFollowInfo;
import com.example.userinfodemo.net.RetrofitManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserFollowListPresenter extends BasePresenter<UserFollowListActivity> implements IUserFollowListContract.IUserFollowListPresenter {

    @Override
    public void start() {

    }

    @Override
    public void queryUserFollowingInfo(String userName, int page, int perPage) {
        RetrofitManager.getInstance().createUsersService().getUserFollowingInfo(userName, page, perPage)
                .compose(RxTransformUtils.schedule())
                .subscribe(new Observer<List<UserFollowInfo>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull List<UserFollowInfo> userFollowInfos) {
                        mView.updateData(userFollowInfos);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d("UserFollowListPresenter", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void queryUserFollowersInfo(String userName, int page, int perPage) {
        RetrofitManager.getInstance().createUsersService().getUserFollowersInfo(userName, page, perPage)
                .compose(RxTransformUtils.schedule())
                .subscribe(new Observer<List<UserFollowInfo>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull List<UserFollowInfo> userFollowInfos) {
                        mView.updateData(userFollowInfos);
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
