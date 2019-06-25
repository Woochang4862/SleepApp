package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;

public class TodayWorkOut {
    private int _id;
    private String title;
    private String startTime;
    private String endTime;
    private boolean isSuccess;

    public TodayWorkOut(int _id, String title, String startTime, String endTime, boolean isSuccess) {
        this._id = _id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSuccess = isSuccess;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
        return "{id:" + _id + ", title:" + title + ", startTime:" + startTime + ", endTime:" + endTime + ", isSuccess:" + isSuccess + "}";
    }
}
