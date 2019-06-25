package com.jeongwoochang.sleepapp.activity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.helper.TimerManager
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.Fragment
import com.jeongwoochang.sleepapp.MyApplication
import com.jeongwoochang.sleepapp.fragment.*
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var sharPrefHelper: SharedPreferencesHelper
    private lateinit var timerManager: TimerManager
    private var timeLeftReceiver: BroadcastReceiver? = null
    private val LOG_TAG = MainActivity::class.java.simpleName
    private val FAST_ALARM_REQUEST_CODE = 1000
    private val FAST_FAILED_CODE = 1001
    private val FAST_SUCCESS_CODE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharPrefHelper = SharedPreferencesHelper(this)
        sharPrefHelper.printAll()

        timerManager = TimerManager(this)

        navigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.action_timer -> selectedFragment = TimerFragment.newInstance()
                R.id.action_ASMR -> selectedFragment = ASMRFragment.newInstance()
                R.id.action_work_out -> selectedFragment = WorkOutFragment.newInstance()
                R.id.action_todo -> selectedFragment = TODOFragment.newInstance()
                R.id.action_info -> selectedFragment = InfoFragment.newInstance()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_content, selectedFragment!!)
            transaction.commit()
            true
        }
        navigation.selectedItemId = R.id.action_timer

        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(this)
        if (dbAdapter.sleepTimes.size != 7) {
            startActivity(Intent(this, SleepTimePreferenceActivity::class.java))
        }
        dbAdapter.close()

        MyApplication.getInstance().setOnVisibilityChangeListener {
            if (it) {
                if (!sharPrefHelper.autoLogin)
                    sharPrefHelper.removeHashSet("cookies")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        timeLeftReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action!!.compareTo(Intent.ACTION_TIME_TICK) == 0) {  //i.e. every minute
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            FAST_SUCCESS_CODE -> {
                Log.d(LOG_TAG, "FAST_SUCCESS_CODE")
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(FAST_ALARM_REQUEST_CODE)
            }
            FAST_FAILED_CODE -> {
                Log.d(LOG_TAG, "FAST_FAILED_CODE")
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(FAST_ALARM_REQUEST_CODE)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }


    private fun checkNotificationPolicy() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(
                android.provider.Settings
                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
            )
            startActivity(intent)
        }
    }
}
