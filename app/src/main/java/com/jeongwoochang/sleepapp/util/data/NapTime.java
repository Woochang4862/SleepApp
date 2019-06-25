package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import timber.log.Timber;

public class NapTime {
    private long date;
    private long napTime;

    public NapTime(long date, long napTime) {
        this.date = date;
        this.napTime = napTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getNapTime() {
        return napTime;
    }

    public void setNapTime(long napTime) {
        this.napTime = napTime;
    }

    public int getDayOfDate(){
        return new DateTime().withMillis(date).dayOfMonth().get();
    }

    public float getMinuteOfNapTime(){
        return (napTime/(float)60000);
    }

    @NonNull
    @Override
    public String toString() {
        String str = "{date:"+date+",napTIme:"+napTime+"}";
        return str;
    }
}
