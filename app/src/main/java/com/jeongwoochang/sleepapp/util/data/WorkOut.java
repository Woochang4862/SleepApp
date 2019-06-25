package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WorkOut {
    private int _id;
    private String title;
    private ArrayList<Boolean> dayOfWeek;
    private String startTime;
    private String endTime;

    public WorkOut(int _id, String title, ArrayList<Boolean> dayOfWeek, String startTime, String endTime) {
        this._id = _id;
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public WorkOut(String title, ArrayList<Boolean> dayOfWeek, String startTime, String endTime) {
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public WorkOut(int _id) {
        this._id = _id;
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

    public ArrayList<Boolean> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(ArrayList<Boolean> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    @NonNull
    @Override
    public String toString() {
        return "{id:" + _id + ", title:" + title + ", dayOfWeek:" + dayOfWeek + ", startTime:" + startTime + ", endTime:" + endTime + "}";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj != null) {
            if(obj instanceof WorkOut)
                return ((WorkOut) obj)._id == _id;
        }
        return false;
    }
}
