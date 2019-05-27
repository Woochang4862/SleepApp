package com.jeongwoochang.sleepapp.activity

import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.fragment.IntervalDialogFragment
import com.jeongwoochang.sleepapp.fragment.NumberOfAlarmsDialogFragment
import com.jeongwoochang.sleepapp.fragment.TimePickerDialogFragment
import com.jeongwoochang.sleepapp.util.data.AlarmParams
import com.jeongwoochang.sleepapp.util.data.AlarmTime
import com.jeongwoochang.sleepapp.util.helper.TimerManager
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import com.jeongwoochang.sleepapp.util.helper.view.AlarmsListHelper
import com.jeongwoochang.sleepapp.util.helper.view.NotificationIconHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IntervalDialogFragment.IntervalDialogListener, NumberOfAlarmsDialogFragment.NumberOfAlarmsDialogListener, TimePickerDialog.OnTimeSetListener {
    
    private var nIconHelper: NotificationIconHelper? = null
    private lateinit var alarmsListHelper: AlarmsListHelper
    private lateinit var sharPrefHelper: SharedPreferencesHelper
    private lateinit var timerManager: TimerManager
    private lateinit var alarmParams: AlarmParams
    private var timeLeftReceiver: BroadcastReceiver? = null
    private val LOG_TAG = MainActivity::class.java!!.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharPrefHelper = SharedPreferencesHelper(this@MainActivity)
        sharPrefHelper.printAll()

        alarmParams = sharPrefHelper.params
        timerManager = TimerManager(this@MainActivity)
        //        nIconHelper = new NotificationIconHelper(MainActivity.this);
        alarmsListHelper = AlarmsListHelper(this@MainActivity, listview_main_alarmslist)

        showFirstAlarmTime(alarmParams.firstAlarmTime.toString())
        showTimeLeft(alarmParams)

        showInterval(sharPrefHelper.intervalStr)
        showNumberOfAlarms(sharPrefHelper.numberOfAlarmsStr)
        switch_main.isChecked = sharPrefHelper.isAlarmTurnedOn
        //        nIconHelper.setNotificationIcon(sharPrefHelper.isAlarmTurnedOn());

        alarmsListHelper.showList(alarmParams)

        switch_main.setOnCheckedChangeListener { buttonView, isChecked ->
            alarmParams.turnedOn = isChecked
            if (isChecked) {
                checkNotificationPolicy()
                //                    timerManager.startTimer(alarmParams);
                timerManager.startSingleAlarmTimer(alarmParams.firstAlarmTime.toMillis())
                //                    nIconHelper.showNotificationIcon();
                showToast(getString(R.string.main_alarm_turned_on_toast))
                sharPrefHelper.numberOfAlreadyRangAlarms = 0
            } else {
                timerManager.cancelTimer()
                //                    nIconHelper.hideNotificationIcon();
                showToast(getString(R.string.main_alarm_turned_off_toast))
            }
            alarmsListHelper.showList(alarmParams)
            showTimeLeft(alarmParams)
            sharPrefHelper.setAlarmState(isChecked)
        }

        layout_main_interval.setOnClickListener {
            val dialog = IntervalDialogFragment()
            val intervalBundle = Bundle()
            intervalBundle.putString("interval", sharPrefHelper.intervalStr)
            dialog.arguments = intervalBundle
            dialog.show(fragmentManager, "intervalDialog")
        }

        layout_main_numberofalarms.setOnClickListener {
            val dialog = NumberOfAlarmsDialogFragment()
            val numberOfAlarmsBundle = Bundle()
            numberOfAlarmsBundle.putString("number_of_alarms", sharPrefHelper.numberOfAlarmsStr)
            dialog.arguments = numberOfAlarmsBundle
            dialog.show(fragmentManager, "numberOfAlarmsDialog")
        }

        layout_main_firstalarm.setOnClickListener {
            val timePickerBundle = Bundle()
            timePickerBundle.putInt("alarm_hour", sharPrefHelper.hour)
            timePickerBundle.putInt("alarm_minute", sharPrefHelper.minute)

            val timePicker = TimePickerDialogFragment()

            timePicker.arguments = timePickerBundle
            timePicker.show(fragmentManager, "time_picker")
        }

        settingBtn.setOnClickListener { startActivity(Intent(baseContext, PreferencesActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        showTimeLeft(alarmParams)
        timeLeftReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action!!.compareTo(Intent.ACTION_TIME_TICK) == 0) {  //i.e. every minute
                    showTimeLeft(alarmParams)
                }
            }
        }
        registerReceiver(timeLeftReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }


    override fun onPause() {
        super.onPause()
        if (timeLeftReceiver != null) {
            unregisterReceiver(timeLeftReceiver)
        }
    }


    override fun onIntervalChanged(intervalStr: String) {
        showInterval(intervalStr)
        alarmParams.interval = Integer.parseInt(intervalStr)
        alarmsListHelper.showList(alarmParams)
        resetTimerIfTurnedOn()
        sharPrefHelper.setInterval(intervalStr)
    }

    override fun onNumberOfAlarmsChanged(numberOfAlarmsStr: String) {
        showNumberOfAlarms(numberOfAlarmsStr)
        alarmParams.numberOfAlarms = Integer.parseInt(numberOfAlarmsStr)
        alarmsListHelper.showList(alarmParams)
        resetTimerIfTurnedOn()
        sharPrefHelper.setNumberOfAlarms(numberOfAlarmsStr)
    }

    override fun onTimeSet(view: TimePicker, hour: Int, minute: Int) {
        val alarmTime = AlarmTime(hour, minute)
        alarmParams.firstAlarmTime = alarmTime
        showFirstAlarmTime(alarmTime.toString())
        alarmsListHelper.showList(alarmParams)
        showTimeLeft(alarmParams)
        sharPrefHelper.numberOfAlreadyRangAlarms = 0
        resetTimerIfTurnedOn()
        sharPrefHelper.time = alarmTime
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun resetTimerIfTurnedOn() {
        if (switch_main.isChecked) {
            //            timerManager.resetTimer(alarmParams);
            timerManager.resetSingleAlarmTimer(alarmParams.firstAlarmTime.toMillis())
            showToast(getString(R.string.main_alarm_reset_toast))
        }
    }

    private fun showInterval(interval: String) {
        val wholeTitle = getString(R.string.main_interval, interval)
        val wholeTitleSpan = SpannableString(wholeTitle)
        wholeTitleSpan.setSpan(RelativeSizeSpan(2f), wholeTitle.indexOf(interval), interval.length + 1, 0)
        textview_main_interval.text = wholeTitleSpan
    }

    private fun showNumberOfAlarms(numberOfAlarms: String) {
        val numberOfAlarmsInt = Integer.parseInt(numberOfAlarms)
        val wholeTitle = this.resources.getQuantityString(R.plurals.main_number_of_alarms, numberOfAlarmsInt, numberOfAlarmsInt)
        val wholeTitleSpan = SpannableString(wholeTitle)
        wholeTitleSpan.setSpan(RelativeSizeSpan(2f), wholeTitle.indexOf(numberOfAlarms),
                numberOfAlarms.length + 1, 0)
        textview_main_numberofalarms.text = wholeTitleSpan
    }

    private fun showFirstAlarmTime(firstAlarmTime: String) {
        val wholeTitle = getString(R.string.main_firstalarm_time, firstAlarmTime)
        val wholeTitleSpan = SpannableString(wholeTitle)
        wholeTitleSpan.setSpan(RelativeSizeSpan(2f), wholeTitle.indexOf(firstAlarmTime) - 1,
                wholeTitle.indexOf(firstAlarmTime) + firstAlarmTime.length, 0)
        textview_main_firstalarm_time.text = wholeTitleSpan
    }

    private fun showTimeLeft(alarmParams: AlarmParams) {
        val alarmTime = alarmParams.firstAlarmTime
        textview_main_timeleft.text = getString(R.string.all_time_left, alarmTime.hoursLeft, alarmTime.minutesLeft)
        if (alarmParams.turnedOn) {
            textview_main_timeleft.setTextColor(resources.getColor(R.color.colorPrimary))
        } else {
            textview_main_timeleft.setTextColor(resources.getColor(R.color.main_disabled_textcolor))
        }
        Log.d(LOG_TAG, "Time left: " + alarmTime.hoursLeft + ":" + alarmTime.minutesLeft)
    }

    private fun checkNotificationPolicy() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }


}
