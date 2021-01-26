package com.example.gratitudejournal.Activities.ui.journal;

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

public class JournalFragment extends Fragment {

    private JournalViewModel mJournalViewModel;

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
}