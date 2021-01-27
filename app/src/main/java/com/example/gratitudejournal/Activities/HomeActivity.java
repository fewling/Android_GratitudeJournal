package com.example.gratitudejournal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.gratitudejournal.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mUserPhoto;
    private TextView mUsername, mUserMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Update the Nav header with user info
        View headerView = mNavigationView.getHeaderView(0);
        mUserPhoto = headerView.findViewById(R.id.nav_user_photo);
        mUsername = headerView.findViewById(R.id.nav_user_name);
        mUserMail = headerView.findViewById(R.id.nav_user_mail);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsername.setText(currentUser.getDisplayName());
        mUserMail.setText(currentUser.getEmail());
        Glide.with(this)
                .load(currentUser.getPhotoUrl())
                .override(80, 80)
                .into(mUserPhoto);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mDrawerLayout)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_journal:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_journal);
                break;

            case R.id.nav_breathe_exercise:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_breathe_exercise);
                break;

            case R.id.nav_questionnaire:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_questionnaire);
                break;

            case R.id.nav_tip:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_tip);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}