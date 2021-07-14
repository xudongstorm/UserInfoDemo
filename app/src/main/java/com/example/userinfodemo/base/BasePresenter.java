package com.example.userinfodemo.base;

public abstract class BasePresenter<V extends IBaseContract.IView> implements IBaseContract.IPresenter<V> {

    protected V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public V getView() {
        return mView;
    }
}
