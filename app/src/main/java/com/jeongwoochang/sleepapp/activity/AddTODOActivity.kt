package com.jeongwoochang.sleepapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import kotlinx.android.synthetic.main.activity_add_todo.*
import org.joda.time.DateTime

class AddTODOActivity : AppCompatActivity() {

    private var onDialogFinishListener: OnDialogFinishListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        saveBtn.setOnClickListener {
            if (!TextUtils.isEmpty(name.text)) {
                val dbAdapter = DBAdapter.getInstance()
                DBAdapter.connect(this)
                val d = DateTime()
                    .withYear(datePicker.year)
                    .withMonthOfYear(datePicker.month + 1)
                    .withDayOfMonth(datePicker.dayOfMonth)
                    .withHourOfDay(0)
                    .withMinuteOfHour(0)
                    .withSecondOfMinute(0)
                    .withMillisOfSecond(0)
                dbAdapter.addTODO(com.jeongwoochang.sleepapp.util.data.TODO(d.millis, name.text.toString()))
                dbAdapter.close()
                onDialogFinishListener?.onDialogFinish()
                    finish()
            } else {
                Snackbar.make(findViewById(android.R.id.content), "이름을 설정해 주세요", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    public fun setOnDialogFinishListener(){
        this.onDialogFinishListener = onDialogFinishListener
    }

    public interface OnDialogFinishListener {
        fun onDialogFinish()
    }
}
