package com.example.userinfodemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.userinfodemo.MyApplication;
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

    private static final String TAG = UserInfoDbManager.class.getSimpleName();

    private volatile static UserInfoDbManager mInstance;
    private UserInfoDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private UserInfoDbManager(){
        mDbHelper = new UserInfoDbHelper(MyApplication.getAppContext());
        mDb = mDbHelper.getWritableDatabase();
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

    public long insertUserInfo(UserInfoDbModel model){
        long ret = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_LOGIN, model.getLogin());
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_AVATARURL, model.getAvatarUrl());
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS, model.getFollowers());
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING, model.getFollowing());
            ret = mDb.insert(UserInfoDbHelper.USER_INFO_TABLE_NAME, null, values);
        }catch (Exception e){
            Log.e(TAG, "insertUserInfo exception: " + e.getMessage());
        }
        return ret;
    }

    public long insertUserFollowInfo(UserFollowInfoDbModel model){
        long ret = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN, model.getLogin());
            values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWERS, model.getFollowers());
            values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWING, model.getFollowing());
            ret = mDb.insert(UserInfoDbHelper.USER_FOLLOW_INFO_TABLE_NAME, null, values);
        }catch (Exception e){
            Log.e(TAG, "insertUserFollowInfo exception: " + e.getMessage());
        }
        return ret;
    }

    public long updateUserInfo(UserInfoDbModel model){
        long ret = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_AVATARURL, model.getAvatarUrl());
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS, model.getFollowers());
            values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING, model.getFollowing());
            ret = mDb.update(UserInfoDbHelper.USER_INFO_TABLE_NAME, values, UserInfoDbHelper.USER_INFO_COLUMN_LOGIN + " = ? ", new String[]{model.getLogin()});
        }catch (Exception e){
            Log.e(TAG, "updateUserInfo exception: " + e.getMessage());
        }
        return ret;
    }

    public long updateUserFollowInfo(UserFollowInfoDbModel model){
        long ret = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWERS, model.getFollowers());
            values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWING, model.getFollowing());
            ret = mDb.update(UserInfoDbHelper.USER_FOLLOW_INFO_TABLE_NAME, values, UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN + " = ? ", new String[]{model.getLogin()});
        }catch (Exception e){
            Log.e(TAG, "updateUserFollowInfo exception: " + e.getMessage());
        }
        return ret;
    }

    public UserFollowInfoDbModel queryUserFollowInfoByLogin(String login){
        if(TextUtils.isEmpty(login)){
            return null;
        }
        Cursor cursor = null;
        try {
            cursor = mDb.query(UserInfoDbHelper.USER_FOLLOW_INFO_TABLE_NAME, null, UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN + " = ?", new String[]{login}, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                String followers = cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS));
                String following = cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING));
                UserFollowInfoDbModel model = new UserFollowInfoDbModel();
                model.setLogin(login);
                model.setFollowers(followers);
                model.setFollowing(following);
                return model;
            }
        }catch (Exception e){
            Log.e(TAG, "UserFollowInfoDbModel exception: " + e.getMessage());
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return null;
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


    public Observable<List<UserFollowInfo>> queryUserFollowInfoByLogin(String login, boolean isFollowers, int page, int perPage){
        if(TextUtils.isEmpty(login)){
            return null;
        }
        return Observable.create(new ObservableOnSubscribe<List<UserFollowInfo>>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<List<UserFollowInfo>> emitter) throws Exception {
                Cursor cursor = null;
                String limit = ((page - 1) * perPage) + "," + perPage;
                cursor = mDb.query(UserInfoDbHelper.USER_FOLLOW_INFO_TABLE_NAME, null, UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN + " = ?", new String[]{login}, null, null, null, limit);
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
