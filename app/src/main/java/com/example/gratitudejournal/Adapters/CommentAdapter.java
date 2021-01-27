package com.example.gratitudejournal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.Models.Comment;
import com.example.gratitudejournal.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Activity mActivity;
    private Context mContext;
    private List<Comment> mCommentList;
    private LayoutInflater mInflater;

    public CommentAdapter(Activity activity, List<Comment> commentList) {
        this.mActivity = activity;
        this.mContext = mActivity.getApplicationContext();
        this.mCommentList = commentList;
        this.mInflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_row, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (!mCommentList.isEmpty()) {
            Glide.with(mActivity)
                    .load(mCommentList.get(holder.getAdapterPosition()).getuImg())
                    .into(holder.mCommentUserImg);
            holder.mCommentUserName.setText(mCommentList.get(holder.getAdapterPosition()).getuName());
            holder.mCommentContent.setText(mCommentList.get(holder.getAdapterPosition()).getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position;
    }

    @Override
    public int getItemCount() {
        if (!mCommentList.isEmpty()) {
            return mCommentList.size();
        } else {
            return 0;
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private ImageView mCommentUserImg;
        private TextView mCommentUserName, mCommentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            mCommentUserImg = itemView.findViewById(R.id.comment_user_img);
            mCommentUserName = itemView.findViewById(R.id.comment_user_name);
            mCommentContent = itemView.findViewById(R.id.comment_content);

        }

    }
}
