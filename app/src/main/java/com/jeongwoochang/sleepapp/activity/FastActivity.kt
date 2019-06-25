package com.jeongwoochang.sleepapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_fast.*
import java.util.*

class FastActivity : AppCompatActivity() {

    private val LOG_TAG = FastActivity::class.java.simpleName
    private var pref: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast)

        pref = SharedPreferencesHelper(this)

        if (pref != null) {
            val c = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            //c.timeInMillis = pref!!.fastTimeBeforeSleep.toLong()

            fastTimeHour.text = Editable.Factory.getInstance().newEditable(c.get(Calendar.HOUR_OF_DAY).toString())
            fastTimeMin.text = Editable.Factory.getInstance().newEditable(c.get(Calendar.MINUTE).toString())
        }

        saveBtn.setOnClickListener {
            if (!fastTimeHour.text.toString().equals("") && !fastTimeMin.text.toString().equals("")) {
                //pref!!.fastTimeBeforeSleep = fastTimeHour.text.toString().toInt() * 3600000 + fastTimeMin.text.toString().toInt() * 60000
                //Log.d(LOG_TAG, "fastTimeBeforeSleep is ${pref!!.fastTimeBeforeSleep}")

                //val timerManager = TimerManager(this)
                //timerManager.setFastTimer(pref!!.fastTimeBeforeSleep)
                //timerManager.close()
            }
        }
    }
}
