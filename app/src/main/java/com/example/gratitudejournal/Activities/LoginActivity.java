package com.example.gratitudejournal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gratitudejournal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton, mRegButton;
    private ProgressBar mLoginProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = findViewById(R.id.loginEmail);
        mPasswordEditText = findViewById(R.id.loginPassword);
        mLoginButton = findViewById(R.id.loginButton);
        mRegButton = findViewById(R.id.toRegButton);
        mRegButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        mLoginProgressBar = findViewById(R.id.loginProgressBar);
        mLoginProgressBar.setVisibility(View.INVISIBLE);

        mLoginButton.setOnClickListener(v -> {
            mLoginButton.setVisibility(View.INVISIBLE);
            mLoginProgressBar.setVisibility(View.VISIBLE);

            final String email = mEmailEditText.getText().toString();
            final String password = mPasswordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                showMessage("Please verify all fields.");
            } else {
                signIn(email, password);
            }
        });
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                mLoginProgressBar.setVisibility(View.INVISIBLE);
                mLoginButton.setVisibility(View.VISIBLE);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
            } else {
                showMessage(task.getException().getMessage());
                mLoginProgressBar.setVisibility(View.INVISIBLE);
                mLoginButton.setVisibility(View.VISIBLE);
            }
        });
    }


    // Auto sign-in
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("current_user", currentUser);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}