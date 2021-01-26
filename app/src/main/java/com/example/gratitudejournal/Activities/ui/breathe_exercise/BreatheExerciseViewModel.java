package com.example.gratitudejournal.Activities.ui.breathe_exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BreatheExerciseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BreatheExerciseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is breathing exercise fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}