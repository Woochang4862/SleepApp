package com.jeongwoochang.sleepapp.util.data

import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Created by Ilya Anshmidt on 13.10.2017.
 */

class AlarmTime(val hour: Int, val minute: Int) {


    //return millisToString(timeDifferenceMillis);
    val timeLeftString: String
        get() {
            val currentTimeMillis = Calendar.getInstance().timeInMillis
            val alarmTimeMillis = toMillis()
            val timeDifferenceMillis = alarmTimeMillis - currentTimeMillis
            return getTimeDifference(timeDifferenceMillis).toString()
        }

    private val millisLeft: Long
        get() {
            val currentTimeMillis = Calendar.getInstance().timeInMillis
            val alarmTimeMillis = toMillis()
            return alarmTimeMillis - currentTimeMillis
        }

    val hoursLeft: String
        get() = String.format("%d", TimeUnit.MILLISECONDS.toHours(millisLeft))

    val minutesLeft: String
        get() {
            val millisLeft = millisLeft
            return String.format(
                "%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisLeft) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millisLeft)
                )
            )
        }

    fun toMillis(): Long {
        val currentTimeMillis = Calendar.getInstance().timeInMillis
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val alarmTimeMillis = calendar.timeInMillis

        // alarm will be set to the next 24 h
        val timeDifferenceMillis = alarmTimeMillis - currentTimeMillis
        if (timeDifferenceMillis < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        if (timeDifferenceMillis > 1000 * 3600 * 24) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
        return calendar.timeInMillis
    }

    fun toNextDayMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }

    private fun millisToString(timeInMillis: Long): String {
        return millisToAlarmTime(timeInMillis).toString()
        //        return String.format("%d:%02d",
        //                TimeUnit.MILLISECONDS.toHours(timeInMillis),
        //                TimeUnit.MILLISECONDS.toMinutes(timeInMillis) -
        //                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis)));
    }

    fun millisToAlarmTime(timeInMillis: Long): AlarmTime {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return AlarmTime(hour, minute)
    }

    fun getTimeDifference(timeDifferenceMillis: Long): AlarmTime {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = timeDifferenceMillis
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return AlarmTime(hour, minute)
    }


    override fun toString(): String {
        val hourToDisplay: String
        val minuteToDisplay: String

        if (hour < 10) {
            hourToDisplay = "0$hour"
        } else {
            hourToDisplay = "" + hour
        }

        if (minute < 10) {
            minuteToDisplay = "0$minute"
        } else {
            minuteToDisplay = "" + minute
        }
        return "$hourToDisplay:$minuteToDisplay"
    }


    fun addMinutes(minutesAdded: Int): AlarmTime {
        val initialTimeMillis = this.toMillis()
        val incrementMillis = (minutesAdded * 60 * 1000).toLong()
        val incrementedTimeMillis = initialTimeMillis + incrementMillis
        return millisToAlarmTime(incrementedTimeMillis)
    }
}
