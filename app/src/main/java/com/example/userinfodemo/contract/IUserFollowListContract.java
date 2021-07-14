package com.example.userinfodemo.contract;

import com.example.userinfodemo.model.UserFollowInfo;

import java.util.List;

import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserFollowListContract {

    interface IUserFollowListView{

        void updateData(List<UserFollowInfo> list);

    }

    interface IUserFollowListPresenter{

        void queryUserFollowingInfo(String userName, int page, int perPage);

        void queryUserFollowersInfo(String userName, int page, int perPage);

    }
}
