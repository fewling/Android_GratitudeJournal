package com.example.gratitudejournal.Activities.ui.tip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TipViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TipViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tip fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}