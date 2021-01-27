package com.example.gratitudejournal.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content;
    private String uid;
    private String uImg;
    private String uName;
    private Object timestamp;
    private String commentKey;

    public Comment() {
    }

    public Comment(String content, String uid, String uImg, String uName, String commentKey) {
        this.content = content;
        this.uid = uid;
        this.uImg = uImg;
        this.uName = uName;
        this.timestamp = ServerValue.TIMESTAMP;
        this.commentKey = commentKey;
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

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
}
