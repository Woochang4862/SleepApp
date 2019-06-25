package com.jeongwoochang.sleepapp.util.data;

import java.util.HashSet;

public class FastAlarm {
    private HashSet<Integer> dayOfWeek;
    private String napStartTime;
    private String napEndTime;
    private Boolean active;

    public FastAlarm(HashSet<Integer> dayOfWeek, String napStartTime, String napEndTime, Boolean active) {
        this.dayOfWeek = dayOfWeek;
        this.napStartTime = napStartTime;
        this.napEndTime = napEndTime;
        this.active = active;
    }

    public HashSet<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(HashSet<Integer> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getNapStartTime() {
        return napStartTime;
    }

    public void setNapStartTime(String napStartTime) {
        this.napStartTime = napStartTime;
    }

    public String getNapEndTime() {
        return napEndTime;
    }

    public void setNapEndTime(String napEndTime) {
        this.napEndTime = napEndTime;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
