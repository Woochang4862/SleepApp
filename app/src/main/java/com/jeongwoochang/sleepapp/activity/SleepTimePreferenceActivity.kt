package com.jeongwoochang.sleepapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import android.view.View
import android.widget.TextView
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.data.SleepTime
import com.jeongwoochang.sleepapp.util.helper.TimerManager
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import kotlinx.android.synthetic.main.activity_sleep_time_preference.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class SleepTimePreferenceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var sleepTimesMap: HashMap<Int, SleepTime>
    private lateinit var sleepTimesArr: ArrayList<SleepTime>
    private lateinit var dayOfWeekBtn: ArrayList<TextView>
    private lateinit var dayOfWeekBtnID: ArrayList<Int>
    private lateinit var selectedDay: HashSet<Int>
    private lateinit var savedDay: HashSet<Int>
    private lateinit var dbAdapter: DBAdapter
    private var isFirstSetting = false
    private val LOG_TAG = SleepTimePreferenceActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_time_preference)

        Timber.plant(Timber.DebugTree())
        Timber.tag(LOG_TAG)

        dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(this)
        sleepTimesArr = dbAdapter.sleepTimes as ArrayList<SleepTime>
        isFirstSetting = sleepTimesArr.isEmpty()
        Timber.d(sleepTimesArr.toString())

        timePicker.setTime(LocalTime.of(23, 0), LocalTime.of(7, 0))

        timePicker.listener = { bedTime: LocalTime, wakeTime: LocalTime ->
            Timber.d("time changed \nbedtime= $bedTime\nwaketime=$wakeTime")
            handleUpdate(bedTime, wakeTime)
        }
        handleUpdate(timePicker.getBedTime(), timePicker.getWakeTime())

        backArrow.setOnClickListener {
            finish()
        }

        dayOfWeekBtn = arrayListOf(
            sunBtn,
            monBtn,
            tueBtn,
            wedBtn,
            thuBtn,
            friBtn,
            satBtn
        )
        dayOfWeekBtnID = arrayListOf(
            R.id.sunBtn,
            R.id.monBtn,
            R.id.tueBtn,
            R.id.wedBtn,
            R.id.thuBtn,
            R.id.friBtn,
            R.id.satBtn
        )
        sleepTimesMap = HashMap()
        selectedDay = HashSet()
        savedDay = HashSet()
        Timber.d("size of dayOfWeekBtn = ${dayOfWeekBtn.size}, size of dayOfWeekBtnID = ${dayOfWeekBtnID.size}")
        next.setOnClickListener {

            if (innerText.text == "Next") {
                selectedDay.toSortedSet().forEach {
                    dayOfWeekBtn[it].visibility = View.GONE
                    sleepTimesMap[it] = SleepTime(tvBedTime.text.toString(), tvWakeTime.text.toString())
                }
                selectedDay.clear()
            } else {
                selectedDay.toSortedSet().forEach {
                    sleepTimesMap[it] = (SleepTime(tvBedTime.text.toString(), tvWakeTime.text.toString()))
                }
                save()
                finish()
            }
            Timber.d(sleepTimesMap.toString())
        }
        dayOfWeekBtn.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        if (selectedDay.contains(dayOfWeekBtnID.indexOf(v?.id))) {
            selectedDay.remove(dayOfWeekBtnID.indexOf(v?.id))
            savedDay.remove(dayOfWeekBtnID.indexOf(v?.id))
            (v as AppCompatTextView).setTextColor(resources.getColor(R.color.dayDeselected))
        } else {
            selectedDay.add(dayOfWeekBtnID.indexOf(v?.id))
            savedDay.add(dayOfWeekBtnID.indexOf(v?.id))
            (v as AppCompatTextView).setTextColor(resources.getColor(R.color.daySelected))
        }
        if (savedDay.size == 7) {
            innerText.text = "Save"
        } else {
            innerText.text = "Next"
        }
        Timber.d("selectedDay = $selectedDay, savedDay = $savedDay")
    }

    private fun save() {
        sleepTimesArr.clear()
        sleepTimesMap.toSortedMap().values.forEach {
            sleepTimesArr.add(it)
        }
        if(isFirstSetting) dbAdapter.initSleepTime(sleepTimesArr)
        else dbAdapter.updateSleepTimes(sleepTimesArr)
        TimerManager(this).setFastTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbAdapter.close()
    }

    private fun handleUpdate(bedTime: LocalTime, wakeTime: LocalTime) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREA)
        tvBedTime.text = bedTime.format(formatter)
        tvWakeTime.text = wakeTime.format(formatter)

        val bedDate = bedTime.atDate(LocalDate.now())
        var wakeDate = wakeTime.atDate(LocalDate.now())
        if (bedDate >= wakeDate) wakeDate = wakeDate.plusDays(1)
        val duration = Duration.between(bedDate, wakeDate)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        tvHours.text = hours.toString()
        tvMins.text = minutes.toString()
        if (minutes > 0) llMins.visibility = View.VISIBLE else llMins.visibility = View.GONE
    }
}
