package com.example.gratitudejournal.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.Models.Comment;
import com.example.gratitudejournal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView mPostImg, mImgAuthor, mImgCurrentUser;
    private TextView mPostDescriptionTextView, mPostDateNameTextView, mPostTitleTextView;
    private EditText mCommentEditText;
    private Button mCommentButton;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        // Set the statue bar transparent
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        mPostImg = findViewById(R.id.post_detail_img);
        mImgAuthor = findViewById(R.id.post_detail_author_photo);
        mImgCurrentUser = findViewById(R.id.post_detail_currentuser_photo);
        mPostDescriptionTextView = findViewById(R.id.post_detail_description);
        mPostDateNameTextView = findViewById(R.id.post_detail_date_name);
        mPostTitleTextView = findViewById(R.id.post_detal_title);
        mCommentEditText = findViewById(R.id.post_detail_comment_edittext);
        mCommentButton = findViewById(R.id.post_detail_add_comment_button);

        // To bind data into these views
        // First to get Post data
        // Send post detail data to this activity...


        String postImage = getIntent().getExtras().getString("post_image");
        Glide.with(this).load(postImage).into(mPostImg);

        String postTitle = getIntent().getExtras().getString("title");
        mPostTitleTextView.setText(postTitle);

        String authorPhoto = getIntent().getExtras().getString("author_photo");
        Glide.with(this).load(authorPhoto).into(mImgAuthor);

        String postDescription = getIntent().getExtras().getString("description");
        mPostDescriptionTextView.setText(postDescription);

        Long timestamp = getIntent().getExtras().getLong("timestamp");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy ");
        String date = format.format(timestamp);

        mPostDateNameTextView.setText("By Author name, " + date);
        Glide.with(this).load(mCurrentUser.getPhotoUrl()).into(mImgCurrentUser);

        String postKey = getIntent().getExtras().getString("post_key");


        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCommentButton.setVisibility(View.INVISIBLE);

                DatabaseReference commentReference = mFirebaseDatabase.getReference("Posts/" + postKey +"/Comment").child(postKey).push();
                String comment_content = mCommentEditText.getText().toString();
                String uid = mCurrentUser.getUid();
                String uname = mCurrentUser.getDisplayName();
                String uimg = mCurrentUser.getPhotoUrl().toString();

                Comment comment = new Comment(comment_content, uid, uimg, uname);

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
            }
        });

    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}