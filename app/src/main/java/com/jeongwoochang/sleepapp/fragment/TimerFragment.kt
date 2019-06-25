package com.jeongwoochang.sleepapp.fragment

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.jeongwoochang.sleepapp.API.APIClient
import com.jeongwoochang.sleepapp.API.APIInterface
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.ARActivity
import com.jeongwoochang.sleepapp.activity.PreferencesActivity
import com.jeongwoochang.sleepapp.adapter.FastAchievementListAdapter
import com.jeongwoochang.sleepapp.util.data.UserRes
import com.jeongwoochang.sleepapp.util.helper.TimerManager
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer.view.*
import kotlinx.android.synthetic.main.fragment_timer.view.swipeView
import kotlinx.android.synthetic.main.fragment_yt_asmr.view.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimerFragment : Fragment() {

    private lateinit var prf: SharedPreferencesHelper
    private lateinit var dbAdapter: DBAdapter
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning: Boolean = false
    private var START_TIME_IN_MILLIS: Long = 0L
    private lateinit var service: APIInterface

    private var mTimeLeftInMillis: Long = 0L

    private lateinit var viewOfLayout: View

    companion object {
        fun newInstance(): TimerFragment {
            val args = Bundle()

            val fragment = TimerFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_timer, container, false)
        service = APIClient.getClient(context).create(APIInterface::class.java)
        prf = SharedPreferencesHelper(context!!)
        service.getUser(prf.email).enqueue(object : Callback<UserRes> {
            override fun onFailure(call: Call<UserRes>, t: Throwable) {

            }

            override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                val data = response.body()
                viewOfLayout.username.text = data!!.username
            }
        })
        dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(activity)
        Timber.plant(DebugTree())
        //viewOfLayout.cameraBtn.setOnClickListener { }
        viewOfLayout.settingBtn.setOnClickListener {
            startActivity(Intent(activity, PreferencesActivity::class.java))
        }
        val cd = DateTime()
        viewOfLayout.currDate.text =
            String.format("%d %s %d", cd.dayOfMonth, DateTimeFormat.forPattern("EEE").print(cd).toUpperCase(), cd.year)

        //Fast Alarm Setting
        viewOfLayout.fastTimerHour.text = prf.fastTimerHour.toString()
        viewOfLayout.fastTimerMinute.text = prf.fastTimerMin.toString()
        viewOfLayout.fastStartTime.text = DateTimeFormat.forPattern("= HH시 mm분").print(TimerManager(context).fastTime)
        viewOfLayout.fastTimeArea.setOnClickListener {
            val timerPickerDialog = TimePickerDialog(
                activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val fmt = DateTimeFormat.forPattern("HH:mm")
                    val dt = DateTime.parse("$hourOfDay:$minute", fmt)
                    dt.withZone(DateTimeZone.UTC)
                    Timber.d(dt.millisOfDay.toString())
                    prf.fastTimer = dt.millisOfDay.toLong()
                    viewOfLayout.fastTimerHour.text = prf.fastTimerHour.toString()
                    viewOfLayout.fastTimerMinute.text = prf.fastTimerMin.toString()
                    TimerManager(context).setFastTimer()
                    val fastTime = TimerManager(context).fastTime
                    viewOfLayout.fastStartTime.text = DateTimeFormat.forPattern("= HH시 mm분").print(fastTime)
                }, prf.fastTimerDateTime.hourOfDay, prf.fastTimerDateTime.minuteOfHour, true
            )
            timerPickerDialog.show()
        }
        val fastAchievementListAdapter = FastAchievementListAdapter(
            activity,
            dbAdapter.fastAchievements
        )
        dbAdapter.close()
        //fast achievement setting
        DBAdapter.connect(activity)
        viewOfLayout.fastAchievementList.adapter = fastAchievementListAdapter
        viewOfLayout.fastAchievementList.layoutManager = LinearLayoutManager(activity)
        var successCount = 0F
        dbAdapter.fastAchievements.forEach {
            if (it.isSuccess)
                successCount++
        }
        val fastAchievementRating = if (dbAdapter.fastAchievements.size == 0) {
            0
        } else {
            successCount / dbAdapter.fastAchievements.size * 100
        }
        Timber.d("fastAchievementRating : $fastAchievementRating%")
        viewOfLayout.achievementRating.text = fastAchievementRating.toString()
        dbAdapter.close()

        //Nap Timer Setting
        resetTimer()
        viewOfLayout.swipeView.setOnRefreshListener {
            refresh()
            viewOfLayout.swipeView.isRefreshing = false
        }
        viewOfLayout.swipeView.setColorSchemeColors(Color.GRAY)
        viewOfLayout.swipeView.setDistanceToTriggerSync(20)
        viewOfLayout.swipeView.setSize(SwipeRefreshLayout.DEFAULT)
        viewOfLayout.pauseBtn.setOnClickListener { pauseTimer() }
        viewOfLayout.playBtn.setOnClickListener { startTimer() }
        viewOfLayout.stopBtn.setOnClickListener {
            val fmt = DateTimeFormat.forPattern("HH:mm:ss").withZone(DateTimeZone.UTC)
            val nd = DateTime.parse(viewOfLayout.napTimer.text.toString(), fmt)
            val d =
                DateTime().withMillis(prf.napTimer - nd.withZone(DateTimeZone.UTC).millis).withZone(DateTimeZone.UTC)
            val dialog = AlertDialog.Builder(context!!)
                .setTitle("")
                .setMessage("지금까지의 숙면 시간 \"${fmt.print(d)}\"를 저장 하시겠습니까?")
                .setPositiveButton("저장") { dialog, which ->
                    DBAdapter.connect(activity)
                    dbAdapter.saveNapTime(fmt.print(d))
                    dbAdapter.close()
                }
                .setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }
            dialog.create().show()
            resetTimer()
        }
        START_TIME_IN_MILLIS = prf.napTimer
        mTimeLeftInMillis = START_TIME_IN_MILLIS
        viewOfLayout.napTimer.text = prf.napTimerString
        viewOfLayout.napTimer.setOnClickListener {
            val timerPickerDialog =
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance({ view, hourOfDay, minute, second ->
                    val fmt = DateTimeFormat.forPattern("HH:mm:ss")
                    val dt = DateTime.parse("$hourOfDay:$minute:$second", fmt)
                    dt.withZone(DateTimeZone.UTC)
                    Timber.d(dt.millisOfDay.toString())
                    prf.napTimer = dt.millisOfDay.toLong()
                    START_TIME_IN_MILLIS = dt.millisOfDay.toLong()
                    mTimeLeftInMillis = START_TIME_IN_MILLIS
                    viewOfLayout.napTimer.text = prf.napTimerString
                }, prf.napTimerDateTime.hourOfDay, prf.napTimerDateTime.minuteOfHour, true)
            timerPickerDialog.enableSeconds(true)
            timerPickerDialog.show(activity!!.fragmentManager, "TimePickerDialog")
        }

        viewOfLayout.nowMonth.text =
            DateTimeFormat.forPattern("MMM").withLocale(Locale.US).print(cd).toUpperCase(Locale.US)

        //Nap Chart Setting
        viewOfLayout.napChart.setViewPortOffsets(60F, 0F, 50F, 60F)
        viewOfLayout.napChart.description.isEnabled = false
        viewOfLayout.napChart.setTouchEnabled(true)
        viewOfLayout.napChart.isDragEnabled = true
        viewOfLayout.napChart.setScaleEnabled(true)
        viewOfLayout.napChart.setPinchZoom(false)
        viewOfLayout.napChart.setDrawGridBackground(false)
        viewOfLayout.napChart.maxHighlightDistance = 300F
        val xAxis = viewOfLayout.napChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val y = viewOfLayout.napChart.axisLeft
        y.textColor = Color.BLACK
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.BLACK
        viewOfLayout.napChart.axisRight.isEnabled = false
        viewOfLayout.napChart.legend.isEnabled = false
        viewOfLayout.napChart.animateXY(2000, 2000)
        val values = ArrayList<Entry>()
        DBAdapter.connect(context)
        dbAdapter.napTimes.forEach {
            values.add(Entry(it.dayOfDate.toFloat(), it.minuteOfNapTime))
        }
        dbAdapter.close()
        setData(values)
        viewOfLayout.napChart.invalidate()
        viewOfLayout.arBtn.setOnClickListener {
            val i =Intent(activity, ARActivity::class.java)
            val v = ArrayList<Double>()
            DBAdapter.connect(context)
            dbAdapter.napTimes.forEach {
                v.add(it.minuteOfNapTime.toDouble())
            }
            dbAdapter.close()
            i.putExtra("data", v)
            startActivity(i)
        }
        return viewOfLayout
    }

    private fun setData(values: ArrayList<Entry>) {

        val set1: LineDataSet

        if (viewOfLayout.napChart.data != null && viewOfLayout.napChart.data.dataSetCount > 0) {
            set1 = viewOfLayout.napChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            viewOfLayout.napChart.data.notifyDataChanged()
            viewOfLayout.napChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "Nap Time 1")

            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.run { setDrawFilled(true) }
            set1.setDrawCircles(false)
            set1.lineWidth = 3f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.BLUE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.rgb(104, 241, 175)
            set1.fillColor = Color.rgb(104, 241, 175)
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter = IFillFormatter { _, _ -> viewOfLayout.napChart.axisLeft.axisMinimum }

            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            viewOfLayout.napChart.data = data
        }
    }

    private fun refresh() {
        DBAdapter.connect(context)
        val cd = DateTime()
        viewOfLayout.currDate.text =
            String.format("%d %s %d", cd.dayOfMonth, DateTimeFormat.forPattern("EEE").print(cd).toUpperCase(), cd.year)

        //Fast Alarm Setting
        viewOfLayout.fastTimerHour.text = prf.fastTimerHour.toString()
        viewOfLayout.fastTimerMinute.text = prf.fastTimerMin.toString()
        viewOfLayout.fastStartTime.text = DateTimeFormat.forPattern("= HH시 mm분").print(TimerManager(context).fastTime)
        val fastAchievementListAdapter = FastAchievementListAdapter(
            activity,
            dbAdapter.fastAchievements
        )
        viewOfLayout.fastAchievementList.adapter = fastAchievementListAdapter
        resetTimer()
        val values = ArrayList<Entry>()
        dbAdapter.napTimes.forEach {
            values.add(Entry(it.dayOfDate.toFloat(), it.minuteOfNapTime))
        }
        dbAdapter.close()
        setData(values)
        viewOfLayout.napChart.invalidate()
    }

    private fun resetTimer() {
        viewOfLayout.napTimer.isEnabled = true
        mTimeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDownText()
        viewOfLayout.pauseBtn.isEnabled = false
        viewOfLayout.pauseBtn.alpha = 0.3f
        viewOfLayout.stopBtn.isEnabled = false
        viewOfLayout.stopBtn.alpha = 0.3f
        viewOfLayout.playBtn.isEnabled = true
        viewOfLayout.playBtn.alpha = 1f
    }

    private fun startTimer() {
        viewOfLayout.napTimer.isEnabled = false
        countDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                Timber.d("NapTimer : Time Out!")
                isTimerRunning = false
                viewOfLayout.playBtn.isEnabled = true
                viewOfLayout.playBtn.alpha = 1f
                viewOfLayout.pauseBtn.isEnabled = false
                viewOfLayout.pauseBtn.alpha = 0.3f
                viewOfLayout.stopBtn.isEnabled = true
                viewOfLayout.stopBtn.alpha = 1f
                resetTimer()
            }
        }.start()

        isTimerRunning = true
        viewOfLayout.playBtn.isEnabled = false
        viewOfLayout.playBtn.alpha = 0.3f
        viewOfLayout.pauseBtn.isEnabled = true
        viewOfLayout.pauseBtn.alpha = 1f
        viewOfLayout.stopBtn.isEnabled = false
        viewOfLayout.stopBtn.alpha = 0.3f
    }

    private fun updateCountDownText() {
        val c = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        c.timeInMillis = mTimeLeftInMillis

        val format = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
        format.timeZone = TimeZone.getTimeZone("UTC")
        viewOfLayout.napTimer.text = format.format(c.time)
    }

    private fun pauseTimer() {
        countDownTimer!!.cancel()
        isTimerRunning = false
        viewOfLayout.pauseBtn.isEnabled = false
        viewOfLayout.pauseBtn.alpha = 0.3f
        viewOfLayout.playBtn.isEnabled = true
        viewOfLayout.playBtn.alpha = 1f
        viewOfLayout.stopBtn.isEnabled = true
        viewOfLayout.stopBtn.alpha = 1f
    }
}