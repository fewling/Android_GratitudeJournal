package com.example.gratitudejournal.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView mPostImg, mImgAuthor, mImgCurrentUser;
    private TextView mPostDescriptionTextView, mPostDateNameTextView, mPostTitleTextView;
    private EditText mCommentEditText;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

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

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(this).load(mCurrentUser.getPhotoUrl()).into(mImgCurrentUser);

        String postKey = getIntent().getExtras().getString("post_key");

    }
}