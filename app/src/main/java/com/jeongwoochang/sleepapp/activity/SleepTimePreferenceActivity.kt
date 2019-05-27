package com.jeongwoochang.sleepapp.activity

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jeongwoochang.sleepapp.R
import kotlinx.android.synthetic.main.activity_sleep_time_preference.*
import java.util.*

class SleepTimePreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_time_preference)

        setStartBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                sleepStartTV.text = "$h : $m"
            }), hour, minute, false)

            tpd.show()
        }
        setEndBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                sleepEndTV.text = "$h : $m"
            }), hour, minute, false)

            tpd.show()
        }

        saveBtn.setOnClickListener {
            if (!sleepStartTV.equals("") && !sleepEndTV.equals("")) {
                var data = Intent(this, PreferencesActivity::class.java)
                data.putExtra(resources.getString(R.string.key_sleep_start_time), sleepStartTV.text.toString())
                data.putExtra(resources.getString(R.string.key_sleep_end_time), sleepEndTV.text.toString())
                setResult(Activity.RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(baseContext, "수면 시작시간 또는 종료시간을 설정해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
