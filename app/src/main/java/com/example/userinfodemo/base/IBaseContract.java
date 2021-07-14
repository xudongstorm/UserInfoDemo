package com.example.userinfodemo.base;

public interface IBaseContract {

    interface IView {

        void showLoading();

        void hideLoading();
    }

    interface IPresenter<T extends IView> {

        void start();

        void attachView(T view);

        void detachView();

        T getView();

    }
}
