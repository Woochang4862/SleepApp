package com.jeongwoochang.sleepapp.util.helper.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.jeongwoochang.sleepapp.R
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ilya Anshmidt on 24.09.2017.
 */

class SharedPreferencesHelper(private val context: Context) {
    private val preferences: SharedPreferences
    private val LOG_TAG = SharedPreferencesHelper::class.java.simpleName
    //Key
    private val FAST_TIMER: String
    private val NAP_TIEMR: String
    private val EMAIL: String
    private val PW: String
    private val AUTO_LOGIN: String
    private val MILLIS_LEFT: String
    private val TIMER_RUNNING: String
    private val END_TIME: String
    //etc
    private val INSTALLATION_DATE: String

    //Default value
    private val DEFAULT_FAST_TIMER = 3600000L
    private val DEFAULT_NAP_TIMER = 3600000L

    //Property
    var fastTimer: Long
        get() = preferences.getLong(FAST_TIMER, DEFAULT_FAST_TIMER)
        set(value) {
            val editor = preferences.edit()
            editor.putLong(FAST_TIMER, value)
            editor.apply()
        }
    val fastTimerDateTime: DateTime
        get() = DateTime(fastTimer, DateTimeZone.UTC)
    val fastTimerString: String
        get() {
            val dt = fastTimerDateTime
            return DateTimeFormat.forPattern("HH:mm").print(dt)
        }
    val fastTimerHour: Int
        get() {
            val dt = fastTimerDateTime
            return dt.hourOfDay
        }
    val fastTimerMin: Int
        get() {
            val dt = fastTimerDateTime
            return dt.minuteOfHour
        }
    var napTimer: Long
        get() = preferences.getLong(NAP_TIEMR, DEFAULT_NAP_TIMER)
        set(value) {
            val editor = preferences.edit()
            editor.putLong(NAP_TIEMR, value)
            editor.apply()
        }
    val napTimerDateTime: DateTime
        get() = DateTime(napTimer, DateTimeZone.UTC)
    val napTimerString: String
        get() {
            val dt = napTimerDateTime
            return DateTimeFormat.forPattern("HH:mm:ss").print(dt)
        }
    var email: String
        get() = preferences.getString(EMAIL, "")
        set(value) {
            val editor = preferences.edit()
            editor.putString(EMAIL, value)
            editor.apply()
        }
    var pw: String
        get() = preferences.getString(PW, "")
        set(value) {
            val editor = preferences.edit()
            editor.putString(PW, value)
            editor.apply()
        }
    var autoLogin: Boolean
        get() = preferences.getBoolean(AUTO_LOGIN, false)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(AUTO_LOGIN, value)
            editor.apply()
        }
    var millisLeft:Long
    get() = preferences.getLong(MILLIS_LEFT, napTimer)
    set(value) {
        val editor = preferences.edit()
        editor.putLong(MILLIS_LEFT, value)
        editor.apply()
    }
    var timerRunning:Boolean
    get() = preferences.getBoolean(TIMER_RUNNING, false)
    set(value) {
        val editor = preferences.edit()
        editor.putBoolean(TIMER_RUNNING, value)
        editor.apply()
    }
    var endTime:Long
    get() = preferences.getLong(END_TIME, 0)
    set(value) {
        val editor = preferences.edit()
        editor.putLong(END_TIME, value)
        editor.apply()
    }
    //etc
    private
    val installationDate: Long
        get() {
            val defaultInstallationDate = Calendar.getInstance().timeInMillis
            if (preferences.contains(INSTALLATION_DATE)) {
                return preferences.getLong(INSTALLATION_DATE, defaultInstallationDate)
            } else {
                Timber.tag(LOG_TAG).d("INSTALLATION_DATE not found")
                setInstallationDate()
                return defaultInstallationDate
            }
        }
    val daysSinceInstallation: Long
        get() {
            val currentDateMillis = Calendar.getInstance().timeInMillis
            val installationDateMillis = installationDate
            val diffDays = TimeUnit.DAYS.convert(currentDateMillis - installationDateMillis, TimeUnit.MILLISECONDS)
            Timber.tag(LOG_TAG).d("Days since installation: $diffDays")
            return diffDays
        }

    init {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context)
        FAST_TIMER = context.resources.getString(R.string.key_fast_timer)
        NAP_TIEMR = context.resources.getString(R.string.key_nap_timer)
        EMAIL = context.resources.getString(R.string.key_email)
        PW = context.resources.getString(R.string.key_pw)
        AUTO_LOGIN = context.resources.getString(R.string.key_auto_login)
        MILLIS_LEFT = context.resources.getString(R.string.key_millis_left)
        TIMER_RUNNING = context.resources.getString(R.string.key_timer_running)
        END_TIME = context.resources.getString(R.string.key_end_time)
        //etc
        INSTALLATION_DATE = context.resources.getString(R.string.key_installation_date)

        if (!preferences.contains(FAST_TIMER)) {
            setDefaultFastTimer()
        }

        if (!preferences.contains(NAP_TIEMR)) {
            setDefaultNapTimer()
        }
    }

    private fun setInstallationDate() {
        val currentDateMillis = Calendar.getInstance().timeInMillis
        val editor = preferences.edit()
        editor.putLong(INSTALLATION_DATE, currentDateMillis)
        editor.apply()
    }

    fun setDefaultFastTimer() {
        val editor = preferences.edit()
        editor.putLong(FAST_TIMER, DEFAULT_FAST_TIMER)
        editor.apply()
    }

    fun setDefaultNapTimer() {
        val editor = preferences.edit()
        editor.putLong(NAP_TIEMR, DEFAULT_NAP_TIMER)
        editor.apply()
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
        preferences.edit().clear().apply()
        Log.d(LOG_TAG, "Shared preferences are deleted")
    }

    public fun putHashSet(key: String, set: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(key, set)
        editor.apply()
    }

    public fun getHashSet(key: String, dftValue: HashSet<String>): HashSet<String> {
        try {
            return preferences.getStringSet(key, dftValue) as HashSet<String>
        } catch (e: Exception) {
            e.printStackTrace()
            return dftValue
        }
    }

    public fun removeHashSet(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }
}
