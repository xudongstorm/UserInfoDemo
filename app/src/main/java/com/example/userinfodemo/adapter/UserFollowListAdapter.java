package com.example.userinfodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userinfodemo.R;
import com.example.userinfodemo.activity.UserInfoActivity;
import com.example.userinfodemo.bean.UserFollowInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserFollowListAdapter extends RecyclerView.Adapter<UserFollowListAdapter.BaseViewHolder> {

    private Context mContext;
    private List<UserFollowInfo> mData;

    private static final int TYPE_FOOT_VIEW = 1000;
    private boolean canLoadMore;      //是否可以上拉加载

    public UserFollowListAdapter(Context context, List<UserFollowInfo> data){
        mContext = context;
        mData = data;
    }

    public void updateDataAndRefresh(List<UserFollowInfo> datas) {
        int previousSize = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, previousSize);
        mData.addAll(datas);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == TYPE_FOOT_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading, parent, false);
            return new UserFollowListAdapter.FootViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_follow_list, parent, false);
            return new UserFollowListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        if(holder instanceof UserFollowListViewHolder){
            ((UserFollowListViewHolder) holder).setData(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + (canLoadMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(canLoadMore){
            if(position == mData.size()){
                return TYPE_FOOT_VIEW;
            }
            return super.getItemViewType(position);
        }
        return super.getItemViewType(position);
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FootViewHolder extends BaseViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    class UserFollowListViewHolder extends BaseViewHolder {

        AppCompatImageView ivAvatar;
        TextView tvName;
        UserFollowInfo userFollowInfo;

        public UserFollowListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(v -> UserInfoActivity.start(mContext, tvName.getText().toString(), userFollowInfo.getAvatar_url()));
        }

        public void setData(UserFollowInfo userFollowInfo){
            if(userFollowInfo != null){
                this.userFollowInfo = userFollowInfo;
                Glide.with(mContext).load(userFollowInfo.getAvatar_url()).into(ivAvatar);
                tvName.setText(userFollowInfo.getLogin());
            }
        }
    }
}
