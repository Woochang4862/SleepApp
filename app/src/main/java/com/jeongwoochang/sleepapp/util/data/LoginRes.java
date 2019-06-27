package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoginRes {
    @SerializedName("status")
    private boolean status;

    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("photo")
    private String photo;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("id", id).append("username", username).append("photo", photo).append("message", message).toString();
    }
}
