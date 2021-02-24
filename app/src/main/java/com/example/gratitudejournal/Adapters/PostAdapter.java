package com.example.gratitudejournal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.Activities.PostDetailActivity;
import com.example.gratitudejournal.Models.Post;
import com.example.gratitudejournal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Post> mPostList;
    private LayoutInflater mInflater;

    ImageView mRowPostImg, mRowPostProfileImg;
    TextView mRowPostTitleTextView;

    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPostList = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.row_post_item, parent, false);
        return new PostViewHolder(mItemView);
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

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {

        String postKey = null;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

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
            intent.putExtra("user_name", mPostList.get(position).getUsername());
            long timestamp = (long) mPostList.get(position).getTimeStamp();
            intent.putExtra("timestamp", timestamp);

            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            showPopupMenu(v);
            int position = getAdapterPosition();
            Post post = mPostList.get(position);
            postKey = post.getPostKey();
            return true;
        }

        private void showPopupMenu(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.list_popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_option:
                    // deletes the post in Firebase:
                    if (postKey != null) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("Posts").child(postKey);
                        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                postKey = null;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "Failed deletion.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
            return false;
        }
    }
}
