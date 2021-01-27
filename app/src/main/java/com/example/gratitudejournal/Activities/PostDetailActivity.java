package com.example.gratitudejournal.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.Adapters.CommentAdapter;
import com.example.gratitudejournal.Models.Comment;
import com.example.gratitudejournal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";
    private ImageView mPostImg, mImgAuthor, mImgCurrentUser;
    private TextView mPostDescriptionTextView, mPostDateNameTextView, mPostTitleTextView;
    private EditText mCommentEditText;
    private Button mCommentButton;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private RecyclerView mCommentRecyclerView;
    private CommentAdapter mCommentAdapter;
    private List<Comment> mCommentList = new LinkedList<>();
    private String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

//        transparentStatueBar();

        initializeViews();
        bindPostData();
        buttonAddComment();
        setupCommentRecyclerView();
    }

    private void transparentStatueBar() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void initializeViews() {
        mPostImg = findViewById(R.id.post_detail_img);
        mImgAuthor = findViewById(R.id.post_detail_author_photo);
        mImgCurrentUser = findViewById(R.id.post_detail_currentuser_photo);
        mPostDescriptionTextView = findViewById(R.id.post_detail_description);
        mPostDateNameTextView = findViewById(R.id.post_detail_date_name);
        mPostTitleTextView = findViewById(R.id.post_detal_title);
        mCommentEditText = findViewById(R.id.post_detail_comment_edittext);
        mCommentButton = findViewById(R.id.post_detail_add_comment_button);
    }

    @SuppressLint("SetTextI18n")
    private void bindPostData() {
        String postImage = getIntent().getExtras().getString("post_image");
        Glide.with(this).load(postImage).into(mPostImg);

        String postTitle = getIntent().getExtras().getString("title");
        mPostTitleTextView.setText(postTitle);

        String authorPhoto = getIntent().getExtras().getString("author_photo");
        Glide.with(this).load(authorPhoto).into(mImgAuthor);

        String postDescription = getIntent().getExtras().getString("description");
        mPostDescriptionTextView.setText(postDescription);

        Long timestamp = getIntent().getExtras().getLong("timestamp");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy ");
        String date = format.format(timestamp);
        String username = getIntent().getExtras().getString("user_name");
        mPostDateNameTextView.setText("By " + username + ", " + date);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(this).load(mCurrentUser.getPhotoUrl()).into(mImgCurrentUser);

    }

    private void buttonAddComment() {
        postKey = getIntent().getExtras().getString("post_key");
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCommentEditText.getText().toString().isEmpty()) {

                    mCommentButton.setVisibility(View.INVISIBLE);
                    // Get post unique ID and update post key
                    DatabaseReference commentReference = mFirebaseDatabase.getReference("Posts/" + postKey + "/Comment").push();
                    String comment_content = mCommentEditText.getText().toString();
                    String uid = mCurrentUser.getUid();
                    String uname = mCurrentUser.getDisplayName();
                    String uimg = mCurrentUser.getPhotoUrl().toString();
                    String key = commentReference.getKey();

                    Comment comment = new Comment(comment_content, uid, uimg, uname, key);

                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mCommentEditText.getText().clear();
                            showMessage(" Comment added");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Fail to add comment." + e.getMessage());
                        }
                    });
                    mCommentButton.setVisibility(View.VISIBLE);
                } else {
                    showMessage("You could not leave a blank comment.");
                }
            }
        });
    }

    private void setupCommentRecyclerView() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Posts/" + postKey + "/Comment");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCommentList.clear();

                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    mCommentList.add(comment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mCommentRecyclerView = findViewById(R.id.post_detail_comment_recyclerviewr);
        mCommentAdapter = new CommentAdapter(PostDetailActivity.this, mCommentList);
        mCommentRecyclerView.setAdapter(mCommentAdapter);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}