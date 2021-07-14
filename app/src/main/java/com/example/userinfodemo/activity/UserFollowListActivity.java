package com.example.userinfodemo.activity;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userinfodemo.R;
import com.example.userinfodemo.adapter.UserFollowListAdapter;
import com.example.userinfodemo.base.BaseActivity;
import com.example.userinfodemo.contract.IUserFollowListContract;
import com.example.userinfodemo.model.UserFollowInfo;
import com.example.userinfodemo.presenter.UserFollowListPresenter;

import java.util.ArrayList;
import java.util.List;

public class UserFollowListActivity extends BaseActivity<UserFollowListPresenter> implements IUserFollowListContract.IUserFollowListView {

    private RecyclerView mRvUserFollow;
    private UserFollowListAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<UserFollowInfo> mList;
    private String mLoginName;
    private boolean isFollowers;

    private static final String INTENT_KEY_LOGIN = "intent_key_login";
    private static final String INTENT_KEY_ISFOLLOWERS = "intent_key_followers";

    public static void start(Context context, String loginName, boolean isFollowers){
        Intent intent = new Intent(context, UserFollowListActivity.class);
        intent.putExtra(INTENT_KEY_LOGIN, loginName);
        intent.putExtra(INTENT_KEY_ISFOLLOWERS, isFollowers);
        context.startActivity(intent);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_user_follow_list;
    }

    @Override
    protected void initView() {
        mRvUserFollow = findViewById(R.id.rv_user_flower);
        mList = new ArrayList<>();
        mManager = new LinearLayoutManager(this);
        mAdapter = new UserFollowListAdapter(this, mList);
        mRvUserFollow.setLayoutManager(mManager);
        mRvUserFollow.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mLoginName = getIntent().getStringExtra(INTENT_KEY_LOGIN);
        isFollowers = getIntent().getBooleanExtra(INTENT_KEY_ISFOLLOWERS, true);
        if(isFollowers){
            mPresenter.queryUserFollowersInfo(mLoginName, 1, 30);
        }else{
            mPresenter.queryUserFollowingInfo(mLoginName, 1, 30);
        }
    }

    @Override
    protected UserFollowListPresenter initPresenter() {
        return new UserFollowListPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateData(List<UserFollowInfo> list) {
        mList = list;
        mAdapter.updateDataAndRefresh(mList);
    }
}
