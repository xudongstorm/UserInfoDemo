package com.example.userinfodemo.contract;

import com.example.userinfodemo.model.UserInfo;

public interface IUserInfoContract {

    interface IUserInfoView {

        void updateData(UserInfo userInfo);

    }

    interface IUserInfoPresenter {

        void getUserInfo(String userName);

    }
}
