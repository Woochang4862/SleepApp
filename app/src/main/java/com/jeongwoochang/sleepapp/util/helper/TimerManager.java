package com.jeongwoochang.sleepapp.util.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jeongwoochang.sleepapp.receiver.FastBroadcastReceiver;
import com.jeongwoochang.sleepapp.receiver.MOLABroadcastReceiver;
import com.jeongwoochang.sleepapp.util.SleepingTime;
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter;
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Created by Ilya Anshmidt on 23.09.2017.
 */

public class TimerManager {
    private Context context;
    private AlarmManager alarmManager;
    private DBAdapter dbAdapter;
    private SharedPreferencesHelper prf;
    private final String LOG_TAG = TimerManager.class.getSimpleName();
    private int FAST_START_ALARM_REQUEST_CODE = 1000;
    private int FAST_END_ALARM_REQUEST_CODE = 1001;

    public TimerManager(Context context) {
        this.context = context;
        this.prf = new SharedPreferencesHelper(context);
        this.alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.dbAdapter = DBAdapter.getInstance();
        DBAdapter.connect(this.context);
    }

    public DateTime getFastTime(){
        DateTime currDate = new DateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        DateTime bedTime = new DateTime();
        bedTime = bedTime
                .withHourOfDay(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(currDate.dayOfWeek().get()).getStart()).hourOfDay().get())
                .withMinuteOfHour(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(currDate.dayOfWeek().get()).getStart()).minuteOfHour().get())
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        if(bedTime.getMillis() < currDate.getMillis()) bedTime = bedTime.plusDays(1);
        DateTime fastTime = bedTime.minusMillis((int) prf.getFastTimer());
        return fastTime;
    }

    public void setFastTimer() {
        //set fast start timer
        DateTime currDate = new DateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        DateTime bedTime = new DateTime();
        bedTime = bedTime
                .withHourOfDay(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(currDate.dayOfWeek().get()).getStart()).hourOfDay().get())
                .withMinuteOfHour(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(currDate.dayOfWeek().get()).getStart()).minuteOfHour().get())
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        if(bedTime.getMillis() < currDate.getMillis()) bedTime = bedTime.plusDays(1);
        DateTime fastTime = bedTime.minusMillis((int) prf.getFastTimer());
        Log.d(LOG_TAG, "Fast Start Alarm is set to " + fastTime.toString() + " diff : " + fastTime.getMillis());

        Intent intent = new Intent(context, FastBroadcastReceiver.class);
        intent.putExtra("isStart", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, FAST_START_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, fastTime.getMillis(), pendingIntent);

        //set fast end timer
        DateTime wakeUpTime = new DateTime();
        wakeUpTime = wakeUpTime
                .withHourOfDay(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(currDate.dayOfWeek().get()).getEnd()).hourOfDay().get())
                .withMinuteOfHour(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(currDate.dayOfWeek().get()).getEnd()).minuteOfHour().get())
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        if(wakeUpTime.getMillis() < currDate.getMillis()) wakeUpTime = wakeUpTime.plusDays(1);
        Log.d(LOG_TAG, "Fast End Alarm is set to " + wakeUpTime.toString() + " diff : " + wakeUpTime.getMillis());

        intent = new Intent(context, FastBroadcastReceiver.class);
        intent.putExtra("isStart", false);
        pendingIntent = PendingIntent.getBroadcast(context, FAST_END_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeUpTime.getMillis(), pendingIntent);
    }

    public void setNextFastStartAlarm() {
        //set fast start timer
        DateTime nextDate = new DateTime().plus(1).withSecondOfMinute(0).withMillisOfSecond(0);
        /*DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        DateTime bedTime = new DateTime();
        bedTime = bedTime
                .withHourOfDay(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(nextDate.dayOfWeek().get()).getStart()).hourOfDay().get())
                .withMinuteOfHour(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(nextDate.dayOfWeek().get()).getStart()).minuteOfHour().get())
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        if(bedTime.getMillis() < nextDate.getMillis()) bedTime.plus(1);
        DateTime fastTime = bedTime.minusMillis((int) prf.getFastTimer());*/
        Log.d(LOG_TAG, "Next Fast Start Alarm is set to " + nextDate.toString() + " diff : " + nextDate.getMillis());

        Intent intent = new Intent(context, FastBroadcastReceiver.class);
        intent.putExtra("isStart", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, FAST_START_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextDate.getMillis(), pendingIntent);
    }

    public void setNextFastEndAlarm() {
        //set fast end timer
        DateTime nextDate = new DateTime().plusDays(1).withSecondOfMinute(0).withMillisOfSecond(0);
        /*DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        DateTime wakeUpTime = new DateTime();
        wakeUpTime = wakeUpTime
                .withHourOfDay(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(nextDate.dayOfWeek().get()).getEnd()).hourOfDay().get())
                .withMinuteOfHour(dateTimeFormatter.parseDateTime(dbAdapter.getSleepTime(nextDate.dayOfWeek().get()).getEnd()).minuteOfHour().get())
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        if(wakeUpTime.getMillis() < nextDate.getMillis()) wakeUpTime.plus(1);*/
        Log.d(LOG_TAG, "Next Fast End Alarm is set to " + nextDate.toString() + " diff : " + nextDate.getMillis());

        Intent intent = new Intent(context, FastBroadcastReceiver.class);
        intent.putExtra("isStart", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, FAST_END_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextDate.getMillis(), pendingIntent);
    }

    public void close() {
        dbAdapter.close();
    }
}
