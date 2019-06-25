package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TODO {
    private int _id;
    private long date;
    private String name;
    private boolean isSuccess;

    public TODO(int _id, long date, String name, boolean isSuccess) {
        this._id = _id;
        this.date = date;
        this.name = name;
        this.isSuccess = isSuccess;
    }

    public TODO(long date, String name) {
        this.date = date;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @NonNull
    @Override
    public String toString() {
        return "{id:" + _id + "date:" + date + ", name:" + name + ",success:" + isSuccess + "}";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null) {
            if (obj instanceof TODO) {
                return ((TODO) obj).get_id() == this._id;
            }
        }
        return false;
    }
}
