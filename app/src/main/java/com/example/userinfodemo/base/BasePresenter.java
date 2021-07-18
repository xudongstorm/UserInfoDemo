package com.example.userinfodemo.base;

import com.example.userinfodemo.common.UserInfoUtil;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends IBaseContract.IView> implements IBaseContract.IPresenter<V> {

    protected V mView;
    protected CompositeDisposable mSubscription = new CompositeDisposable();

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        UserInfoUtil.unsubscribe(mSubscription);
        mView = null;
    }

    @Override
    public V getView() {
        return mView;
    }
}
