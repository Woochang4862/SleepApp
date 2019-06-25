package com.jeongwoochang.sleepapp.util.data;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepTime {
    private String pattern = "HH:mm";
    private String start;
    private String end;

    public SleepTime(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Calendar getStartTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.KOREA);
        try {
            Date d = simpleDateFormat.parse(start);
            c.set(Calendar.HOUR_OF_DAY, d.getHours());
            c.set(Calendar.MINUTE, d.getMinutes());
            c.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar curr = Calendar.getInstance();
        if(curr.getTimeInMillis() < getEndTime().getTimeInMillis()){
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        return c;
    }

    public Calendar getEndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.KOREA);
        try {
            Date d = simpleDateFormat.parse(end);
            c.set(Calendar.HOUR_OF_DAY, d.getHours());
            c.set(Calendar.MINUTE, d.getMinutes());
            c.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar curr = Calendar.getInstance();
        if (curr.get(Calendar.HOUR_OF_DAY) > c.get(Calendar.HOUR_OF_DAY)) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        } else if (curr.get(Calendar.HOUR_OF_DAY) == c.get(Calendar.HOUR_OF_DAY)) {
            if (curr.get(Calendar.MINUTE) > c.get(Calendar.MINUTE)) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return c;
    }

    public long getDiffStartAndCurrent() {
        Calendar c;
        if ((c = getStartTime()) == null) return -1;
        return c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    }

    public long getDiffEndAndCurrent() {
        Calendar c;
        if ((c = getEndTime()) == null) return -1;
        return c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    }

    public boolean isSleepTime(){
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "{ "+start+" ~ "+end+" }";
    }
}
