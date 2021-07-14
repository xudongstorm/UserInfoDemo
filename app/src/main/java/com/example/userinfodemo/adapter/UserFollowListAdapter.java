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
import com.example.userinfodemo.model.UserFollowInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserFollowListAdapter extends RecyclerView.Adapter<UserFollowListAdapter.UserFollowListViewHolder> {

    private Context mContext;
    private List<UserFollowInfo> mData;

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
    public UserFollowListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_follow_list, parent, false);
        return new UserFollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserFollowListViewHolder holder, int position) {
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class UserFollowListViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivAvatar;
        TextView tvName;
        UserFollowInfo userFollowInfo;

        public UserFollowListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(v -> UserInfoActivity.start(mContext, tvName.getText().toString()));
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
