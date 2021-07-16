package com.example.userinfodemo.contract;

import com.example.userinfodemo.bean.UserInfo;

public interface IUserInfoContract {

    interface IUserInfoView {

        void updateData(UserInfo userInfo);

    }

    interface IUserInfoPresenter {

        void getUserInfo(String userName);

    }
}
