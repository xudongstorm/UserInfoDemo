package com.example.userinfodemo.net;

import com.example.userinfodemo.bean.UserFollowInfo;
import com.example.userinfodemo.bean.UserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsersService {

    @GET("{path}")
    Observable<UserInfo> getUserInfo(@Path("path") String userName);

    @GET("{path}/following")
    Observable<List<UserFollowInfo>> getUserFollowingInfo(@Path("path") String userName, @Query("page") int page, @Query("per_page")int perPage);

    @GET("{path}/followers")
    Observable<List<UserFollowInfo>> getUserFollowersInfo(@Path("path") String userName, @Query("page") int page, @Query("per_page")int perPage);

}
