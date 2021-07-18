package com.example.userinfodemo.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<P extends IBaseContract.IPresenter> extends AppCompatActivity implements IBaseContract.IView {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        setContentView(getResourceId());
        initView();
        initData();
    }

    protected abstract int getResourceId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
