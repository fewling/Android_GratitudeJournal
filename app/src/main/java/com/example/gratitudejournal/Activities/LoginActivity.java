package com.example.gratitudejournal.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gratitudejournal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.setVisibility(View.INVISIBLE);
                mLoginProgressBar.setVisibility(View.VISIBLE);

                final String email = mEmailEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Please verify all fields.");
                } else {
                    signIn(email, password);
                }
            }
        });
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    mLoginProgressBar.setVisibility(View.INVISIBLE);
                    mLoginButton.setVisibility(View.VISIBLE);

                    updateUI();
                } else {
                    showMessage(task.getException().getMessage());
                    mLoginProgressBar.setVisibility(View.INVISIBLE);
                    mLoginButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}