package com.example.userinfodemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.userinfodemo.MyApplication;
import com.example.userinfodemo.model.UserFollowInfoDbModel;
import com.example.userinfodemo.model.UserInfoDbModel;

public class UserInfoDbManager {

    private volatile static UserInfoDbManager mInstance;
    private UserInfoDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private UserInfoDbManager(){
        mDbHelper = new UserInfoDbHelper(MyApplication.getAppContext(), UserInfoDbHelper.DB_NAME, null, 1);
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

    public void insertUserInfo(UserInfoDbModel model){
        ContentValues values = new ContentValues();
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_LOGIN, model.getLogin());
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_AVATARURL, model.getAvatarUrl());
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS, model.getFollowers());
        values.put(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING, model.getFollowing());
        mDb.insert(UserInfoDbHelper.DB_NAME, null, values);
    }

    public void insertUserFollowInfo(UserFollowInfoDbModel model){
        ContentValues values = new ContentValues();
        values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN, model.getLogin());
        values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWERS, model.getFollowers());
        values.put(UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_FOLLOWING, model.getFollowing());
        mDb.insert(UserInfoDbHelper.DB_NAME, null, values);
    }

    public UserInfoDbModel queryUserInfoByLogin(String login){
        if(TextUtils.isEmpty(login)){
            return null;
        }
        Cursor cursor = mDb.query(UserInfoDbHelper.DB_NAME, null, UserInfoDbHelper.USER_INFO_COLUMN_LOGIN + " = ?", new String[]{login}, null, null, null);
        UserInfoDbModel model = null;
        if(cursor != null && cursor.moveToFirst()){
            model = new UserInfoDbModel();
            model.setLogin(login);
            model.setAvatarUrl(cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_AVATARURL)));
            model.setFollowers(cursor.getInt(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS)));
            model.setFollowing(cursor.getInt(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING)));
            cursor.close();
        }
        return model;
    }

    public UserFollowInfoDbModel queryUserFollowInfoByLogin(String login){
        if(TextUtils.isEmpty(login)){
            return null;
        }
        Cursor cursor = mDb.query(UserInfoDbHelper.DB_NAME, null, UserInfoDbHelper.USER_FOLLOW_INFO_COLUMN_LOGIN + " = ?", new String[]{login}, null, null, null);
        UserFollowInfoDbModel model = null;
        if(cursor != null && cursor.moveToFirst()){
            model = new UserFollowInfoDbModel();
            model.setLogin(login);
            model.setFollowers(cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWERS)));
            model.setFollowing(cursor.getString(cursor.getColumnIndex(UserInfoDbHelper.USER_INFO_COLUMN_FOLLOWING)));
            cursor.close();
        }
        return model;
    }


}
