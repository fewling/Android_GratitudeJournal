package com.example.gratitudejournal.Activities.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gratitudejournal.R;

public class BreatheExerciseFragment extends Fragment implements View.OnClickListener {

    private static final int BREATHE_TIME = 5;
    private ProgressBar mProgressBar;
    private EditText mUserTimeEditText;
    private int animationCounts = 0;
    private BreatheBarAnimation animation;
    private Boolean cancelled = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_breathing_exercise, container, false);

        mProgressBar = root.findViewById(R.id.breathe_progress_bar);
        mUserTimeEditText = root.findViewById(R.id.breathe_time_edittext);
        Button startButton = root.findViewById(R.id.breathe_start_button);
        Button stopButton = root.findViewById(R.id.breathe_stop_button);
        Button m2MinButton = root.findViewById(R.id._2_min_button);
        Button m10MinButton = root.findViewById(R.id._10_min_button);
        Button m20MinButton = root.findViewById(R.id._20_min_button);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        m2MinButton.setOnClickListener(this);
        m10MinButton.setOnClickListener(this);
        m20MinButton.setOnClickListener(this);

        return root;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.breathe_start_button:
                if (mUserTimeEditText.getText() != null) {
                    String timeStr = String.valueOf(mUserTimeEditText.getText());
                    int time = Integer.parseInt(timeStr) * 60;
                    animationCounts = 0;
                    cancelled = false;
                    animateBar(0, 100, time);
                }
                break;

            case R.id.breathe_stop_button:
                cancelled = true;
                animation.cancel();
                break;

            case R.id._2_min_button:
                animationCounts = 0;
                cancelled = false;
                animateBar(0, 100, 2 * 60);
                break;

            case R.id._10_min_button:
                animationCounts = 0;
                cancelled = false;
                animateBar(0, 100, 10 * 60);
                break;

            case R.id._20_min_button:
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=8oGosHzF9gU&t=18s");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
        }
    }

    private void animateBar(float from, float to, int totalTime) {
        animation = new BreatheBarAnimation(mProgressBar, from, to);
        animation.setDuration(BREATHE_TIME * 1000);
        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationCounts++;
                float start, end;
                if (from == 0) {
                    start = 100;
                    end = 0;
                } else {
                    start = 0;
                    end = 100;
                }

                if (animationCounts < totalTime / BREATHE_TIME && !cancelled) {
                    animateBar(start, end, totalTime);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animation.setAnimationListener(listener);
        mProgressBar.startAnimation(animation);
    }
}