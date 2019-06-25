package com.jeongwoochang.sleepapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPickerRecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.AddWorkOutActivity
import com.jeongwoochang.sleepapp.activity.EditWorkOutActivity
import com.jeongwoochang.sleepapp.adapter.TodayWorkOutListAdapter
import com.jeongwoochang.sleepapp.adapter.WorkOutListAdapter
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import kotlinx.android.synthetic.main.fragment_work_out.*
import kotlinx.android.synthetic.main.fragment_work_out.view.*
import kotlinx.android.synthetic.main.fragment_work_out.view.datePicker
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.util.*

class WorkOutFragment : Fragment(), DatePickerListener {

    private lateinit var viewOfLayout: View
    private lateinit var todayWorkOutListAdapter: TodayWorkOutListAdapter
    private lateinit var workOutListAdapter: WorkOutListAdapter

    companion object {
        fun newInstance(): WorkOutFragment {
            val args = Bundle()

            val fragment = WorkOutFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_work_out, container, false)
        viewOfLayout.addBtn.setOnClickListener { startActivity(Intent(activity, AddWorkOutActivity::class.java)) }
        viewOfLayout.editBtn.setOnClickListener { startActivity(Intent(activity, EditWorkOutActivity::class.java)) }
        val cd = DateTime()
        viewOfLayout.datePicker
            .setListener(this)
            .setDays(cd.dayOfMonth().maximumValue - cd.dayOfMonth + 1)
            .setOffset(0)
            .setDateSelectedColor(ContextCompat.getColor(activity!!, R.color.todo_primary_color))
            .setDateSelectedTextColor(Color.WHITE)
            .setTodayButtonTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            .setTodayDateTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            .setTodayDateTextColor(Color.WHITE)
            .setTodayDateBackgroundColor(ContextCompat.getColor(activity!!, R.color.todo_primary_dark_color))
            .setDayOfWeekTextColor(ContextCompat.getColor(activity!!, R.color.main_textcolor))
            .setUnselectedDayTextColor(ContextCompat.getColor(activity!!, R.color.white))
            .showTodayButton(false)
            .setMonthAndYearTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            .setTodayButtonTextColor(ContextCompat.getColor(activity!!, R.color.main_textcolor))
            .init()
        viewOfLayout.datePicker.setDate(DateTime())
        viewOfLayout.datePicker.backgroundColor = ContextCompat.getColor(activity!!, R.color.default_background_color)
        (((viewOfLayout.datePicker.getChildAt(0) as LinearLayout).getChildAt(1) as FrameLayout).getChildAt(0) as HorizontalPickerRecyclerView).afterMeasured {
            Handler().postDelayed({
                (((viewOfLayout.datePicker.getChildAt(0) as LinearLayout).getChildAt(1) as FrameLayout).getChildAt(0) as HorizontalPickerRecyclerView).findViewHolderForAdapterPosition(
                    0
                )!!.itemView.performClick()
            }, 40)
        }

        viewOfLayout.workOutList.layoutManager = LinearLayoutManager(context)
        todayWorkOutListAdapter = TodayWorkOutListAdapter(activity)
        todayWorkOutListAdapter.setOnCheckedChangeListener { id, isChecked ->
            val dbAdapter = DBAdapter.getInstance()
            DBAdapter.connect(activity)
            dbAdapter.setTodayWorkOutSuccess(id, isChecked)
            dbAdapter.close()
        }
        workOutListAdapter = WorkOutListAdapter(context!!)
        viewOfLayout.workOutList.adapter = todayWorkOutListAdapter
        return viewOfLayout
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        viewOfLayout.dayOfWeek.text = DateTimeFormat.forPattern("E요일").withLocale(Locale.KOREA).print(dateSelected)
        viewOfLayout.date.text = DateTimeFormat.forPattern("dd MM월 yyyy").withLocale(Locale.KOREA).print(dateSelected)
        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(activity)
        if (dateSelected!!.dayOfMonth == DateTime().dayOfMonth) {
            viewOfLayout.workOutList.adapter = todayWorkOutListAdapter
            todayWorkOutListAdapter.setItems(dbAdapter.todayWorkOut)
            todayWorkOutListAdapter.notifyDataSetChanged()
        } else {
            viewOfLayout.workOutList.adapter = workOutListAdapter
            workOutListAdapter.setItems(dbAdapter.getWorkOut(dateSelected.dayOfWeek))
            workOutListAdapter.notifyDataSetChanged()
        }
        dbAdapter.close()
    }

    private inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }

    private fun refresh(){
        datePicker.setDate(DateTime())
        (((viewOfLayout.datePicker.getChildAt(0) as LinearLayout).getChildAt(1) as FrameLayout).getChildAt(0) as HorizontalPickerRecyclerView).afterMeasured {
            Handler().postDelayed({
                (((viewOfLayout.datePicker.getChildAt(0) as LinearLayout).getChildAt(1) as FrameLayout).getChildAt(0) as HorizontalPickerRecyclerView).findViewHolderForAdapterPosition(
                    0
                )!!.itemView.performClick()
            }, 1)
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}