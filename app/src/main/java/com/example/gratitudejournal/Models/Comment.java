package com.example.gratitudejournal.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content;
    private String uid;
    private String uImg;
    private String uName;
    private Object timestamp;

    public Comment(String content, String uid, String uImg, String uName) {
        this.content = content;
        this.uid = uid;
        this.uImg = uImg;
        this.uName = uName;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public String getuImg() {
        return uImg;
    }

    public String getuName() {
        return uName;
    }

    public Object getTimestamp() {
        return timestamp;
    }
}
