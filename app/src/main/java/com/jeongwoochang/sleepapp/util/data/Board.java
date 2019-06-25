package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable{
    @SerializedName("no")
    private int no;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("create_date")
    private String date;
    @SerializedName("body_text")
    private String body_text;
    @SerializedName("photo")
    private String photo;
    @SerializedName("likes")
    private ArrayList<String> likes;

    public Board(int no, String id, String title, String date, String body_text, String photo, ArrayList<String> likes) {
        this.no = no;
        this.id = id;
        this.title = title;
        this.date = date;
        this.body_text = body_text;
        this.photo = photo;
        this.likes = likes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody_text() {
        return body_text;
    }

    public void setBody_text(String body_text) {
        this.body_text = body_text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

}
