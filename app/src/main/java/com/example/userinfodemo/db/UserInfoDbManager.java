package com.example.userinfodemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.userinfodemo.bean.UserFollowInfo;
import com.example.userinfodemo.bean.UserFollowInfoDbModel;
import com.example.userinfodemo.bean.UserFollowPersonInfo;
import com.example.userinfodemo.bean.UserInfo;
import com.example.userinfodemo.bean.UserInfoDbModel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class UserInfoDbManager {

    private volatile static UserInfoDbManager mInstance;
    private UserInfoDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private UserInfoDbManager(){
        /*mDbHelper = new UserInfoDbHelper(MyApplication.getAppContext());
        mDb = mDbHelper.getWritableDatabase();*/
    }

    public static UserInfoDbManager getInstance(){
        if(mInstance == null){
            synchronized (UserInfoDbManager.class){
                if(mInstance == null){
                    mInstance = new UserInfoDbManager();
                }
            }
        }
        return mInstance;
    }

    public void createTable(Context context){
        mDbHelper = new UserInfoDbHelper(context.getApplicationContext());
        mDb = mDbHelper.getWritableDatabase();
    }

    public void insertUserInfo(UserInfoDbModel model){
        ContentValues values = new ContentValues();
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_LOGIN, model.getLogin());
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_AVATARURL, model.getAvatarUrl());
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS, model.getFollowers());
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING, model.getFollowing());
        mDb.insert(UserInfoDbHelper.USER_INFO_TABLE_NAME, null, values);
    }

    public void insertUserFollowInfo(UserFollowInfoDbModel model){
        ContentValues values = new ContentValues();
        values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN, model.getLogin());
        values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWERS, model.getFollowers());
        values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWING, model.getFollowing());
        mDb.insert(UserInfoDbHelper.USER_FOLLOW_INFO_TABLE_NAME, null, values);
    }

    public Observable<UserInfo> queryUserInfoByLogin(String login){
        if(TextUtils.isEmpty(login)){
            return null;
        }
        return Observable.create(new ObservableOnSubscribe<UserInfo>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<UserInfo> emitter) throws Exception {
                Cursor cursor = null;
                UserInfo model = null;
                cursor = mDb.query(UserInfoDbHelper.USER_INFO_TABLE_NAME, null, UserInfoDbHelper.USER_INFO_COLUMN_LOGIN + " = ?", new String[]{login}, null, null, null);
                if(cursor != null && cursor.moveToFirst()){
                    model = new UserInfo();
                    model.setLogin(login);
                    model.setAvatar_url(cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_AVATARURL)));
                    model.setFollowers(cursor.getInt(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS)));
                    model.setFollowing(cursor.getInt(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING)));
                    emitter.onNext(model);
                    cursor.close();
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<List<UserFollowInfo>> queryUserFollowInfoByLogin(String login, boolean isFollowers){
        if(TextUtils.isEmpty(login)){
            return null;
        }
        return Observable.create(new ObservableOnSubscribe<List<UserFollowInfo>>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<List<UserFollowInfo>> emitter) throws Exception {
                Cursor cursor = null;
                cursor = mDb.query(UserInfoDbHelper.USER_FOLLOW_INFO_TABLE_NAME, null, UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN + " = ?", new String[]{login}, null, null, null);
                if(cursor != null && cursor.moveToFirst()){
                    String followers = cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS));
                    String following = cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING));
                    List<UserFollowInfo> userFollowInfoList = new ArrayList<>();
                    Gson gson = new Gson();
                    if(isFollowers){
                        if(!TextUtils.isEmpty(followers)){
                            String[] followersArr = followers.split("、");
                            for(String follower : followersArr){
                                UserFollowPersonInfo personInfo = gson.fromJson(follower, UserFollowPersonInfo.class);
                                UserFollowInfo userFollowInfo = new UserFollowInfo();
                                userFollowInfo.setAvatar_url(personInfo.getAvatarUrl());
                                userFollowInfo.setLogin(personInfo.getLogin());
                                userFollowInfoList.add(userFollowInfo);
                            }
                            emitter.onNext(userFollowInfoList);
                        }
                    }else{
                        if(!TextUtils.isEmpty(following)){
                            String[] followeingArr = following.split("、");
                            for(String follower : followeingArr){
                                UserFollowPersonInfo personInfo = gson.fromJson(follower, UserFollowPersonInfo.class);
                                UserFollowInfo userFollowInfo = new UserFollowInfo();
                                userFollowInfo.setAvatar_url(personInfo.getAvatarUrl());
                                userFollowInfo.setLogin(personInfo.getLogin());
                                userFollowInfoList.add(userFollowInfo);
                            }
                            emitter.onNext(userFollowInfoList);
                        }
                    }
                    emitter.onComplete();
                    cursor.close();
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


}
