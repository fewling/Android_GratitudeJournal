package com.example.gratitudejournal.Models;

import com.google.firebase.database.ServerValue;

import java.util.UUID;

public class Post {

    private String title;
    private String description;
    private String picture;
    private UUID userId;
    private String userPhoto;
    private Object timeStamp;

    public Post(String title, String description, String picture, UUID userId, String userPhoto, Object timeStamp) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post() {

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }
}
