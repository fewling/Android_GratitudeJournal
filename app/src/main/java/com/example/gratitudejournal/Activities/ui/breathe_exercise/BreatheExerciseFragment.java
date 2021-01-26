package com.example.gratitudejournal.Activities.ui.breathe_exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gratitudejournal.R;

public class BreatheExerciseFragment extends Fragment {

    private BreatheExerciseViewModel mBreatheExerciseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBreatheExerciseViewModel =
                new ViewModelProvider(this).get(BreatheExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_breathing_exercise, container, false);
        final TextView textView = root.findViewById(R.id.text_breathe_exercise);
        mBreatheExerciseViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}