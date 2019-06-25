package com.jeongwoochang.sleepapp.util;

import android.content.Context;
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter;
import org.joda.time.DateTime;

import java.util.Calendar;

public class SleepingTime {

    private DateProvider dateProvider = DateProvider.SYSTEM;

    HourMinute bedTime;
    HourMinute wakeUpTime;

    public SleepingTime(Context context) {
        DBAdapter dbAdapter = DBAdapter.getInstance();
        DBAdapter.connect(context);
        Calendar bedTimeO = dbAdapter.getSleepTime(DateTime.now().dayOfWeek().get()).getStartTime();
        Calendar wakeUpTimeO = dbAdapter.getSleepTime(DateTime.now().dayOfWeek().get()).getEndTime();
        bedTime = new HourMinute(bedTimeO.get(Calendar.HOUR_OF_DAY),bedTimeO.get(Calendar.MINUTE));
        wakeUpTime = new HourMinute(wakeUpTimeO.get(Calendar.HOUR_OF_DAY),wakeUpTimeO.get(Calendar.MINUTE));
        dbAdapter.close();
    }

    public HourMinute getBedtime() {
        return bedTime;
    }

    public HourMinute getWakeUp() {
        return wakeUpTime;
    }

    protected void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    public boolean isSleepingTime() {
        HourMinute now = new HourMinute(dateProvider.now());

        if (getBedtime().isBeforeMidnight()) {
            int minutesUntilMidnight = getBedtime().getMinutesUntilMidnight();

            HourMinute bedTimeWithOffset = getBedtime().addMinutes(minutesUntilMidnight);
            HourMinute wakeUpTimeWithOffset = getWakeUp().addMinutes(minutesUntilMidnight);
            HourMinute nowWithOffset = now.addMinutes(minutesUntilMidnight);

            return nowWithOffset.isAfter(bedTimeWithOffset) && (nowWithOffset.isBefore(wakeUpTimeWithOffset) || nowWithOffset.equals(wakeUpTimeWithOffset));
        } else {
            return now.isAfter(getBedtime()) && (now.isBefore(getWakeUp()) || now.equals(getWakeUp()));
        }
    }
}
