package com.example.userinfodemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.userinfodemo.common.Constants;
import com.example.userinfodemo.R;
import com.example.userinfodemo.base.BaseActivity;
import com.example.userinfodemo.contract.IUserInfoContract;
import com.example.userinfodemo.bean.UserInfo;
import com.example.userinfodemo.presenter.UserInfoPresenter;

public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements IUserInfoContract.IUserInfoView, View.OnClickListener {

    private AppCompatImageView mIvAvatar;
    private TextView mTvName;
    private TextView mTvFollowers;
    private TextView mTvFollowing;
    private UserInfo mUserInfo;

    private static final String INTENT_KEY_LOGINNAME = "loginName";
    private static final String INTENT_KEY_AVATAR_URL = "avatarUrl";

    public static void start(Context context, String loginName, String avatarUrl){
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(INTENT_KEY_LOGINNAME, loginName);
        intent.putExtra(INTENT_KEY_AVATAR_URL, avatarUrl);
        context.startActivity(intent);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvName = findViewById(R.id.tv_name);
        mTvFollowers = findViewById(R.id.tv_followers);
        mTvFollowing = findViewById(R.id.tv_followeing);

        mTvFollowers.setOnClickListener(this);
        mTvFollowing.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String loginName = getIntent().getStringExtra(INTENT_KEY_LOGINNAME);
        String avatarUrl = getIntent().getStringExtra(INTENT_KEY_AVATAR_URL);
        loginName = TextUtils.isEmpty(loginName) ? Constants.DEFAULT_LOGIN_NAME : loginName;
        mTvName.setText(loginName);
        if(!TextUtils.isEmpty(avatarUrl)){
            Glide.with(this).asBitmap().load(avatarUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).placeholder(R.drawable.personal_default_avatar).error(R.drawable.personal_default_avatar).into(mIvAvatar);
        }
        mPresenter.getUserInfo(loginName);
    }

    @Override
    protected UserInfoPresenter initPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateData(UserInfo userInfo) {
        if(userInfo != null){
            mUserInfo = userInfo;
            Glide.with(this).asBitmap().load(userInfo.getAvatar_url()).apply(RequestOptions.bitmapTransform(new CircleCrop())).placeholder(R.drawable.personal_default_avatar).error(R.drawable.personal_default_avatar).into(mIvAvatar);
            mTvName.setText(userInfo.getLogin());
            mTvFollowing.setText(String.format(getResources().getString(R.string.user_info_followeing_msg), String.valueOf(userInfo.getFollowing())));
            mTvFollowers.setText(String.format(getResources().getString(R.string.user_info_followers_msg), String.valueOf(userInfo.getFollowers())));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(mUserInfo == null){  //避免网络异常时的空指针
            return;
        }
        if(id == R.id.tv_followers){
            UserFollowListActivity.start(this, mUserInfo.getLogin(), true);
        }else if(id == R.id.tv_followeing){
            UserFollowListActivity.start(this, mUserInfo.getLogin(), false);
        }
    }
}
