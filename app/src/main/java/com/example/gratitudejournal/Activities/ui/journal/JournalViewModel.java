package com.example.gratitudejournal.Activities.ui.journal;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JournalViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Uri mUri;

    public JournalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is journal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }
}