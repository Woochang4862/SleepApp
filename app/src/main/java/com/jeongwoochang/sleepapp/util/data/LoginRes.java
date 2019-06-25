package com.jeongwoochang.sleepapp.util.data;

import com.google.gson.annotations.SerializedName;

public class LoginRes {
    @SerializedName("status")
    private boolean status;

    @SerializedName("id")
    private String id;

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
}
