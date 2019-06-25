package com.jeongwoochang.sleepapp.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.AddWorkOutActivity
import com.tayfuncesur.stepper.Stepper
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_work_out_frequency.*
import timber.log.Timber


class WorkOutFrequencyFragment : Fragment(), View.OnClickListener {

    private var nextIsEnabled: Boolean = false
    private lateinit var dayOfWeekBtnList: ArrayList<TextView>
    private lateinit var dayOfweekClickedList: ArrayList<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_work_out_frequency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dayOfWeekBtnList = arrayListOf(
            sunBtn,
            monBtn,
            tueBtn,
            wedBtn,
            thuBtn,
            friBtn,
            satBtn
        )

        dayOfweekClickedList = ArrayList()
        dayOfWeekBtnList.forEach {
            it.setOnClickListener(this)
            dayOfweekClickedList.add(false)
        }

        PushDownAnim.setPushDownAnimTo(next).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            if (nextIsEnabled) {
                view.findNavController().navigate(R.id.fragmentWorkOutFrequencyToTime)
                activity?.findViewById<Stepper>(R.id.stepper)?.forward()
                (activity as AddWorkOutActivity).dayOfWeekClickedList = dayOfweekClickedList
            } else {
                Snackbar.make(view, "운동할 요일을 설정해 주십시오", Snackbar.LENGTH_SHORT).show()
            }
        }

        PushDownAnim.setPushDownAnimTo(backBtn).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            view.findNavController().popBackStack()
            activity?.findViewById<Stepper>(R.id.stepper)?.back()
        }
    }

    override fun onClick(v: View?) {
        if (dayOfWeekBtnList.contains(v as TextView)) {
            if (dayOfweekClickedList[dayOfWeekBtnList.indexOf(v)]) {
                v.paintFlags = v.paintFlags xor Paint.UNDERLINE_TEXT_FLAG
                dayOfweekClickedList[dayOfWeekBtnList.indexOf(v)] = false
            } else {
                v.paintFlags = v.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                dayOfweekClickedList[dayOfWeekBtnList.indexOf(v)] = true
            }
            nextIsEnabled = !isAllFalse(dayOfweekClickedList)
            Timber.d("dayOfweekClickedList = $dayOfweekClickedList")
        }
    }

    private fun isAllFalse(list: ArrayList<Boolean>): Boolean {
        list.forEach {
            if (it) return false
        }
        return true
    }
}
