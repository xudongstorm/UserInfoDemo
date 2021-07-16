package com.example.userinfodemo.net;

import android.util.ArrayMap;

import com.example.userinfodemo.bean.UserFollowInfo;
import com.example.userinfodemo.bean.UserFollowInfoDbModel;
import com.example.userinfodemo.bean.UserInfo;
import com.example.userinfodemo.bean.UserInfoDbModel;
import com.example.userinfodemo.common.UserInfoUtil;
import com.example.userinfodemo.db.UserInfoDbManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserInfoNet {

    private volatile static UserInfoNet mInstance;

    private UserInfoNet(){}

    public static UserInfoNet getInstance(){
        if(mInstance == null){
            synchronized (UserInfoNet.class){
                if(mInstance == null){
                    mInstance = new UserInfoNet();
                }
            }
        }
        return mInstance;
    }

    public Observable<UserInfo> queryUserInfo(String userName){
        return RetrofitManager.getInstance().createUsersService().getUserInfo(userName)
                .map(new Function<UserInfo, UserInfo>() {
                    @Override
                    public UserInfo apply(@NotNull UserInfo userInfo) throws Exception {
                        UserInfoDbModel model = new UserInfoDbModel();
                        model.setLogin(userInfo.getLogin());
                        model.setAvatarUrl(userInfo.getAvatar_url());
                        model.setFollowers(userInfo.getFollowers());
                        model.setFollowing(userInfo.getFollowing());
                        UserInfoDbManager.getInstance().insertUserInfo(model);
                        return userInfo;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public  Observable<List<UserFollowInfo>> queryUserFollowingInfo(String userName, int page, int perPage){
        return RetrofitManager.getInstance().createUsersService().getUserFollowingInfo(userName, page, perPage)
                .map(new Function<List<UserFollowInfo>, List<UserFollowInfo>>() {
                    @Override
                    public List<UserFollowInfo> apply(@NotNull List<UserFollowInfo> userFollowInfos) throws Exception {
                        UserFollowInfoDbModel model = new UserFollowInfoDbModel();
                        model.setLogin(userName);
                        StringBuilder sb = new StringBuilder();
                        for(UserFollowInfo userFollowInfo : userFollowInfos){
                            Map<String, String> map = new ArrayMap<>();
                            map.put("login", userFollowInfo.getLogin());
                            map.put("avatarUrl", userFollowInfo.getAvatar_url());
                            sb.append(UserInfoUtil.mapToJson(map));
                            sb.append("、");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        model.setFollowing(sb.toString());
                        UserInfoDbManager.getInstance().insertUserFollowInfo(model);
                        return userFollowInfos;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public  Observable<List<UserFollowInfo>> queryUserFollowersInfo(String userName, int page, int perPage){
        return RetrofitManager.getInstance().createUsersService().getUserFollowersInfo(userName, page, perPage)
                .map(new Function<List<UserFollowInfo>, List<UserFollowInfo>>() {
                    @Override
                    public List<UserFollowInfo> apply(@NotNull List<UserFollowInfo> userFollowInfos) throws Exception {
                        UserFollowInfoDbModel model = new UserFollowInfoDbModel();
                        model.setLogin(userName);
                        StringBuilder sb = new StringBuilder();
                        for(UserFollowInfo userFollowInfo : userFollowInfos){
                            Map<String, String> map = new ArrayMap<>();
                            map.put("login", userFollowInfo.getLogin());
                            map.put("avatarUrl", userFollowInfo.getAvatar_url());
                            sb.append(UserInfoUtil.mapToJson(map));
                            sb.append("、");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        model.setFollowers(sb.toString());
                        UserInfoDbManager.getInstance().insertUserFollowInfo(model);
                        return userFollowInfos;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
