package com.example.userinfodemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserInfoDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "userInfo.db";

    public static final String USER_INFO_TABLE_NAME = "UserInfo";
    public static final String USER_INFO_COLUMN_LOGIN = "login";
    public static final String USER_INFO_COLUMN_AVATARURL = "avatarUrl";
    public static final String USER_INFO_COLUMN_FOLLOWERS = "followers";
    public static final String USER_INFO_COLUMN_FOLLOWING = "following";

    public static final String USER_FOLLOW_INFO_TABLE_NAME = "UserFollowInfo";
    public static final String USER_FOLLOW_INFO_COLUMN_LOGIN = "login";
    public static final String USER_FOLLOW_INFO_COLUMN_FOLLOWERS = "followers";
    public static final String USER_FOLLOW_INFO_COLUMN_FOLLOWING = "following";

    private static final String CREATE_USER_INFO_TABLE = "create table if not exists " + USER_INFO_TABLE_NAME +
            " (id integer primary key, login text not null, avatarUrl text, followers integer, following integer)";

    private static final String CREATE_USER_FOLLOW_INFO_TABLE = "create table if not exists " + USER_FOLLOW_INFO_TABLE_NAME +
            " (id integer primary key, login text  not null, followers text, following text)";

    public UserInfoDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_INFO_TABLE);
        db.execSQL(CREATE_USER_FOLLOW_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
