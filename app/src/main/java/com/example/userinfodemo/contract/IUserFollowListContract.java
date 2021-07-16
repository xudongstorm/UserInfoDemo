package com.example.userinfodemo.contract;

import com.example.userinfodemo.bean.UserFollowInfo;

import java.util.List;

public interface IUserFollowListContract {

    interface IUserFollowListView{

        void updateData(List<UserFollowInfo> list);

    }

    interface IUserFollowListPresenter{

        void queryUserFollowersInfo(String userName, int page, int perPage);

        void queryUserFollowingInfo(String userName, int page, int perPage);

    }
}
