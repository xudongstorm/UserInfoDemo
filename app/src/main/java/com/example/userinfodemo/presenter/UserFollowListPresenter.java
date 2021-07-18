package com.example.userinfodemo.presenter;

import android.text.TextUtils;
import android.util.Log;

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

    private static final String TAG = UserFollowListPresenter.class.getSimpleName();

    @Override
    public void start() {

    }

    @Override
    public void queryUserFollowersInfo(String userName, int page, int perPage) {
        if(TextUtils.isEmpty(userName) || page <= 0 || perPage <= 0){
            return;
        }
        Log.d(TAG, "queryUserFollowersInfo: userName - " + userName + ", page - " + page + ", perPage - " + perPage);
        Observable.concat(UserInfoDbManager.getInstance().queryUserFollowInfoByLogin(userName, true, page, perPage),
                UserInfoNet.getInstance().queryUserFollowersInfo(userName, page, perPage))
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Observer<List<UserFollowInfo>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NotNull List<UserFollowInfo> userInfo) {
                        mView.updateData(userInfo, page, perPage);
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
        Log.d(TAG, "queryUserFollowingInfo: userName - " + userName + ", page - " + page + ", perPage - " + perPage);
        Observable.concat(UserInfoDbManager.getInstance().queryUserFollowInfoByLogin(userName, false, page, perPage),
                UserInfoNet.getInstance().queryUserFollowingInfo(userName, page, perPage))
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Observer<List<UserFollowInfo>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NotNull List<UserFollowInfo> userInfo) {
                        mView.updateData(userInfo, page, perPage);
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
