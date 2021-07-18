package com.example.userinfodemo.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userinfodemo.R;
import com.example.userinfodemo.adapter.UserFollowListAdapter;
import com.example.userinfodemo.base.BaseActivity;
import com.example.userinfodemo.common.Constants;
import com.example.userinfodemo.contract.IUserFollowListContract;
import com.example.userinfodemo.bean.UserFollowInfo;
import com.example.userinfodemo.presenter.UserFollowListPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserFollowListActivity extends BaseActivity<UserFollowListPresenter> implements IUserFollowListContract.IUserFollowListView {

    private static final String TAG = UserFollowListActivity.class.getSimpleName();

    private RecyclerView mRvUserFollow;
    private UserFollowListAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<UserFollowInfo> mList;
    private String mLoginName;
    private boolean isFollowers;

    private boolean isSlidingUpward;    //是否正在上滑动
    private boolean isLoadFinish;       //是否加载完成

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
        mRvUserFollow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(isSlidingUpward){    //上滑
                        if(!isLoadFinish){
                            int lastItemPosition = mManager.findLastCompletelyVisibleItemPosition();
                            int itemCount = mManager.getItemCount();
                            // 判断是否滑动到了最后一个item，并且是向上滑动
                            if (lastItemPosition == itemCount - 1) {
                                //加载更多
                                onLoadMore();
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingUpward = dy > 0;
            }
        });
    }

    @Override
    protected void initData() {
        mLoginName = getIntent().getStringExtra(INTENT_KEY_LOGIN);
        isFollowers = getIntent().getBooleanExtra(INTENT_KEY_ISFOLLOWERS, true);
        if(isFollowers){
            mPresenter.queryUserFollowersInfo(mLoginName, mList.size() / Constants.USER_INFO_PAGE_SIZE + 1, Constants.USER_INFO_PAGE_SIZE);
        }else{
            mPresenter.queryUserFollowingInfo(mLoginName, mList.size() / Constants.USER_INFO_PAGE_SIZE + 1, Constants.USER_INFO_PAGE_SIZE);
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
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateData(List<UserFollowInfo> list, int page, int perPage) {
        Log.d(TAG, "updateData: list.size - " + list.size() + ", page - " + page + ", perPage - " + perPage);
        if(list.isEmpty()){
            isLoadFinish = true;
            return;
        }if(list.size() < perPage){
            isLoadFinish = true;
        }
        if(mList.size() == (page - 1) * perPage){
            mList.addAll(list);
        }else{  //网络更新
            int start = (page - 1) & perPage;
            for(int i=0; i<list.size(); i++){
                mList.set(start + i, list.get(i));
            }
        }
        if(mAdapter.isCanLoadMore()){
            mAdapter.setCanLoadMore(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    //加载下一页
    private void onLoadMore(){
        if(mAdapter.isCanLoadMore()){
            return;
        }
        mAdapter.setCanLoadMore(true);
        mAdapter.notifyDataSetChanged();
        if(isFollowers){
            mPresenter.queryUserFollowersInfo(mLoginName, mList.size() / Constants.USER_INFO_PAGE_SIZE + 1, Constants.USER_INFO_PAGE_SIZE);
        }else{
            mPresenter.queryUserFollowingInfo(mLoginName, mList.size() / Constants.USER_INFO_PAGE_SIZE + 1, Constants.USER_INFO_PAGE_SIZE);
        }
    }
}
