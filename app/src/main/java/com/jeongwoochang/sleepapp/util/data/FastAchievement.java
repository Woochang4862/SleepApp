package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class FastAchievement {
    private String day;
    private boolean success;

    public FastAchievement(long day, boolean success) {
        DateTime d = new DateTime().withMillis(day);
        this.day = DateTimeFormat.forPattern("dd일 E").print(d);
        this.success = success;
    }

    public FastAchievement(String day, boolean success) {
        this.day = day;
        this.success = success;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @NonNull
    @Override
    public String toString() {
        return day+" : "+success;
    }
}
