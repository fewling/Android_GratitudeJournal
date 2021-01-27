package com.example.gratitudejournal.Activities.ui.journal;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.Models.Post;
import com.example.gratitudejournal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class JournalFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int GALLERY_INTENT_REQUEST_CODE = 2;
    private static final String TAG = "JournalFragment";
    private JournalViewModel mJournalViewModel;
    private Dialog popAddPost;
    private ImageView mPopupPostImage, mPopupAddImage, mPopupUserPhoto;
    private EditText mPopupTitle, mPopupDescription;
    private ProgressBar mPopupProgressBar;
    private Uri pickedImgUri;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iniPopup();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
                setupPopupImageClick();
                setupPopupAddClick();
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mJournalViewModel =
                new ViewModelProvider(this).get(JournalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_journal, container, false);

        final TextView textView = root.findViewById(R.id.text_journal);

        mJournalViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    private void iniPopup() {
        popAddPost = new Dialog(this.getContext());
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        popAddPost.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;

        mPopupPostImage = popAddPost.findViewById(R.id.popup_img_to_upload);
        mPopupTitle = popAddPost.findViewById(R.id.popup_title);
        mPopupDescription = popAddPost.findViewById(R.id.popup_description);
        mPopupAddImage = popAddPost.findViewById(R.id.popup_add);

        mPopupProgressBar = popAddPost.findViewById(R.id.popup_progressBar);
        mPopupProgressBar.setVisibility(View.INVISIBLE);

        mPopupUserPhoto = popAddPost.findViewById(R.id.popup_user_image);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        Glide.with(this)
                .load(currentUser.getPhotoUrl())
                .into(mPopupUserPhoto);
    }

    private void setupPopupImageClick() {

        mPopupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Open the gallery when this image got clicked
                // Before opening the gallery, we need to check if out app have the access to user files
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });
    }


    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessage("Please accept for required permission");

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            openGallery();
        }
    }


    private void openGallery() {
        // TODO: open gallery intent and wait for user to pick an image
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/");
        startActivityForResult(galleryIntent, GALLERY_INTENT_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_INTENT_REQUEST_CODE
                && data != null) {

            // The user has successfully picked an image.
            // We need to save its reference to a Uri variable.
            pickedImgUri = data.getData();
            mPopupPostImage.setImageURI(pickedImgUri);
        }
    }


    private void setupPopupAddClick() {
        mPopupAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopupAddImage.setVisibility(View.INVISIBLE);
                mPopupProgressBar.setVisibility(View.VISIBLE);

                // Make sure all input fields filled and image selected
                if (mPopupTitle.getText().toString().isEmpty()
                        || mPopupDescription.getText().toString().isEmpty()
                        || pickedImgUri == null) {

                    // Ask user to verify all fields...
                    showMessage("Please verify all fields");
                    mPopupProgressBar.setVisibility(View.INVISIBLE);
                    mPopupAddImage.setVisibility(View.VISIBLE);

                } else {
                    // Everything is okay
                    // TODO: Create Post Object and add it to firebase database

                    // First upload the post image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    // Create Post object

                                    Post post = new Post(mPopupTitle.getText().toString(),
                                            mPopupDescription.getText().toString(),
                                            imageDownloadLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());

                                    addPost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Something goes wrong
                                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                                    showMessage(e.getMessage());
                                    mPopupProgressBar.setVisibility(View.INVISIBLE);
                                    mPopupAddImage.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });


                }

            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Posts").push();

        // Get post unique ID and update post key
        String key = databaseReference.getKey();
        post.setPostKey(key);

        // Add post data to firebase database
        databaseReference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added");
                mPopupProgressBar.setVisibility(View.INVISIBLE);
                mPopupAddImage.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });

    }


    private void showMessage(String msg) {
        Toast.makeText(requireActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}