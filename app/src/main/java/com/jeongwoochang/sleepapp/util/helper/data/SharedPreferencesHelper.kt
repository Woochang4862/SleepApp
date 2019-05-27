package com.jeongwoochang.sleepapp.util.helper.data

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.data.AlarmParams
import com.jeongwoochang.sleepapp.util.data.AlarmTime

import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Created by Ilya Anshmidt on 24.09.2017.
 */

class SharedPreferencesHelper(private val context: Context) {
    private val preferences: SharedPreferences
    private val LOG_TAG = SharedPreferencesHelper::class.java.simpleName
    private val FIRST_ALARM_HOUR: String
    private val FIRST_ALARM_MINUTE: String
    private val SWITCH_STATE: String
    private val INTERVAL: String
    private val NUMBER_OF_ALARMS: String
    private val DURATION: String
    private val INSTALLATION_DATE: String
    private val SLEEP_START_TIME: String
    private val SLEEP_END_TIME: String
    private val NUMBER_OF_ALREADY_RANG_ALARMS: String
    private val RINGTONE_FILE_NAME: String

    private val DEFAULT_INTERVAL = 10
    private val DEFAULT_FIRST_ALARM_HOUR = 6
    private val DEFAULT_FIRST_ALARM_MINUTE = 0
    private val DEFAULT_NUMBER_OF_ALARMS = 5
    private val DEFAULT_RINGTONE_FILE_NAME = ""
    private val DEFAULT_SLEEP_START_TIME = 0
    private val DEFAULT_SLEEP_END_TIME = 0

    val hour: Int
        get() = preferences.getInt(FIRST_ALARM_HOUR, DEFAULT_FIRST_ALARM_HOUR)

    val minute: Int
        get() = preferences.getInt(FIRST_ALARM_MINUTE, DEFAULT_FIRST_ALARM_MINUTE)

    var time: AlarmTime
        get() = AlarmTime(hour, minute)
        set(time) {
            val editor = preferences.edit()
            editor.putInt(FIRST_ALARM_HOUR, time.hour)
            editor.putInt(FIRST_ALARM_MINUTE, time.minute)
            editor.apply()
        }

    val isAlarmTurnedOn: Boolean
        get() = preferences.getBoolean(SWITCH_STATE, false)

    //minutes
    val interval: Int
        get() = preferences.getInt(INTERVAL, DEFAULT_INTERVAL)

    val intervalStr: String
        get() = Integer.toString(interval)


    val numberOfAlarms: Int
        get() = preferences.getInt(NUMBER_OF_ALARMS, DEFAULT_NUMBER_OF_ALARMS)

    val numberOfAlarmsStr: String
        get() = Integer.toString(numberOfAlarms)

    val durationStr: String
        get() = preferences.getString(DURATION, context.getString(R.string.preferences_default_duration))

    val durationInt: Int
        get() {
            val durationStr = durationStr
            var durationInt = 0
            if (durationStr.contains("seconds")) {
                val durationParts = durationStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                try {
                    durationInt = Integer.parseInt(durationParts[0])
                } catch (e: NumberFormatException) {
                    Log.e(LOG_TAG, "Invalid duration value")
                }

            }
            Log.d(LOG_TAG, "DurationInt = $durationInt")
            return durationInt
        }

    private
    val installationDate: Long
        get() {
            val defaultInstallationDate = Calendar.getInstance().timeInMillis
            if (preferences.contains(INSTALLATION_DATE)) {
                return preferences.getLong(INSTALLATION_DATE, defaultInstallationDate)
            } else {
                Log.d(LOG_TAG, "INSTALLATION_DATE not found")
                setInstallationDate()
                return defaultInstallationDate
            }
        }

    val daysSinceInstallation: Long
        get() {
            val currentDateMillis = Calendar.getInstance().timeInMillis
            val installationDateMillis = installationDate
            val diffDays = TimeUnit.DAYS.convert(currentDateMillis - installationDateMillis, TimeUnit.MILLISECONDS)
            Log.d(LOG_TAG, "Days since installation: $diffDays")
            return diffDays
        }

    var numberOfAlreadyRangAlarms: Int
        get() = preferences.getInt(NUMBER_OF_ALREADY_RANG_ALARMS, 0)
        set(numberOfAlreadyRangAlarms) {
            val editor = preferences.edit()
            editor.putInt(NUMBER_OF_ALREADY_RANG_ALARMS, numberOfAlreadyRangAlarms)
            editor.apply()
        }

    //example of ringtoneFileName: amelie.mp3
    var ringtoneFileName: String
        get() = preferences.getString(RINGTONE_FILE_NAME, DEFAULT_RINGTONE_FILE_NAME)
        set(fileName) {
            val editor = preferences.edit()
            editor.putString(RINGTONE_FILE_NAME, fileName)
            editor.apply()
            Log.d(LOG_TAG, "ringtoneFileName set to $fileName")
        }


    // content://settings/system/alarm_alert
    // it could happen if user has never set alarm on a new device
    val defaultRingtoneUri: Uri?
        get() {
            var defaultRingtoneUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            if (defaultRingtoneUri == null) {
                defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
            return defaultRingtoneUri
        }

    val params: AlarmParams
        get() = AlarmParams(isAlarmTurnedOn, time, interval, numberOfAlarms)

    init {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context)
        FIRST_ALARM_HOUR = context.resources.getString(R.string.key_first_alarm_hour)
        FIRST_ALARM_MINUTE = context.resources.getString(R.string.key_first_alarm_minute)
        SWITCH_STATE = context.resources.getString(R.string.key_switch_state)
        INTERVAL = context.resources.getString(R.string.key_interval)
        NUMBER_OF_ALARMS = context.resources.getString(R.string.key_number_of_alarms)
        DURATION = context.resources.getString(R.string.key_duration_of_ringtone)
        INSTALLATION_DATE = context.resources.getString(R.string.key_installation_date)
        SLEEP_START_TIME = context.resources.getString(R.string.key_sleep_start_time)
        SLEEP_END_TIME = context.resources.getString(R.string.key_sleep_end_time)
        NUMBER_OF_ALREADY_RANG_ALARMS = context.resources.getString(R.string.key_number_of_already_rang_alarms)
        RINGTONE_FILE_NAME = context.resources.getString(R.string.key_ringtone_filename)

        if (!preferences.contains(NUMBER_OF_ALREADY_RANG_ALARMS)) {
            numberOfAlreadyRangAlarms = 0
        }

        if (!preferences.contains(RINGTONE_FILE_NAME)) {
            setDefaultRingtoneFileName()
        }
    }

    fun doPreferencesExist(): Boolean {
        return preferences.contains(FIRST_ALARM_HOUR)
    }

    fun setTime(hour: Int, minute: Int) {
        val editor = preferences.edit()
        editor.putInt(FIRST_ALARM_HOUR, hour)
        editor.putInt(FIRST_ALARM_MINUTE, minute)
        editor.apply()
    }

    fun setAlarmState(switchState: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(SWITCH_STATE, switchState)
        editor.apply()
    }


    fun setInterval(intervalStr: String) {
        val interval = Integer.parseInt(intervalStr)
        val editor = preferences.edit()
        editor.putInt(INTERVAL, interval)
        editor.apply()
    }

    fun setNumberOfAlarms(numberOfAlarmsStr: String) {
        val numberOfAlarms = Integer.parseInt(numberOfAlarmsStr)
        val editor = preferences.edit()
        editor.putInt(NUMBER_OF_ALARMS, numberOfAlarms)
        editor.apply()
    }

    private fun setInstallationDate() {
        val currentDateMillis = Calendar.getInstance().timeInMillis
        val editor = preferences.edit()
        editor.putLong(INSTALLATION_DATE, currentDateMillis)
        editor.apply()
    }

    private fun setSleepStartTime() {

    }

    fun setDefaultRingtoneFileName() {
        ringtoneFileName = DEFAULT_RINGTONE_FILE_NAME
    }

    fun printAll() {
        val keys = preferences.all
        Log.d(LOG_TAG, "Printing all shared preferences...")
        if (keys != null) {
            for ((key, value) in keys) {
                Log.d(
                    LOG_TAG, key + ": " +
                            value.toString()
                )
            }
            Log.d(LOG_TAG, "End of all preferences.")
        } else {
            Log.d(LOG_TAG, "Shared preferences don't exist")
        }
    }

    fun deleteAll() {
        preferences.edit().clear().commit()
        Log.d(LOG_TAG, "Shared preferences are deleted")
    }


}
