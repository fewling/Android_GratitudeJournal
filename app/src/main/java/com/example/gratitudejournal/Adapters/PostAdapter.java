package com.example.gratitudejournal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.Activities.PostDetailActivity;
import com.example.gratitudejournal.Models.Post;
import com.example.gratitudejournal.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Post> mPostList;
    private LayoutInflater mInflater;

    ImageView mRowPostImg, mRowPostProfileImg;
    TextView mRowPostTitleTextView;
    PostAdapter mAdapter;

    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPostList = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.row_post_item, parent, false);
        return new PostViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Glide.with(mContext).load(mPostList.get(holder.getAdapterPosition()).getPicture()).into(mRowPostImg);
        Glide.with(mContext).load(mPostList.get(holder.getAdapterPosition()).getUserPhoto()).into(mRowPostProfileImg);
        mRowPostTitleTextView.setText(mPostList.get(holder.getAdapterPosition()).getTitle());
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position;
    }


    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public PostViewHolder(@NonNull View itemView, PostAdapter postAdapter) {
            super(itemView);

            mAdapter = postAdapter;
            itemView.setOnClickListener(this);

            mRowPostImg = itemView.findViewById(R.id.row_post_img);
            mRowPostProfileImg = itemView.findViewById(R.id.row_post_profile_img);
            mRowPostTitleTextView = itemView.findViewById(R.id.row_post_title);

        }

        @Override
        public void onClick(View v) {
//            Dialog dialog = new Dialog(mContext);
//            dialog.setContentView(R.layout.show_large_image);
//            dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
//            ImageView imageView = dialog.findViewById(R.id.large_imageView);
//            Glide.with(mContext).load(Uri.parse(mPostList.get(getAdapterPosition()).getPicture())).into(imageView);
//            dialog.show();

            Intent intent = new Intent(mContext, PostDetailActivity.class);
            int position = getAdapterPosition();

            intent.putExtra("title", mPostList.get(position).getTitle());
            intent.putExtra("post_image", mPostList.get(position).getPicture());
            intent.putExtra("author_photo", mPostList.get(position).getUserPhoto());
            intent.putExtra("description", mPostList.get(position).getDescription());
            intent.putExtra("post_key", mPostList.get(position).getPostKey());
//            intent.putExtra("user_name", mPostList.get(position).getUsername());
            long timestamp = (long) mPostList.get(position).getTimeStamp();
            intent.putExtra("timestamp", timestamp);

            mContext.startActivity(intent);
        }
    }
}
