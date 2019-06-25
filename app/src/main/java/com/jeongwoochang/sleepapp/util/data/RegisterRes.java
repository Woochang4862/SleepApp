package com.jeongwoochang.sleepapp.util.data;

import com.google.gson.annotations.SerializedName;

public class RegisterRes {
    @SerializedName("status")
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
