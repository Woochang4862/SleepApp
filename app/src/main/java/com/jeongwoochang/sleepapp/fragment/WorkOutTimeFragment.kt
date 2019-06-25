package com.jeongwoochang.sleepapp.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.AddWorkOutActivity
import com.tayfuncesur.stepper.Stepper
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_work_out_time.*
import java.util.regex.Pattern

class WorkOutTimeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_work_out_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workOutStartTime.setOnClickListener {
            val timerPickerDialog = TimePickerDialog(
                activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    workOutStartTime.text = "%02d:%02d".format(hourOfDay, minute)
                }, workOutStartTime.text.split(":".toRegex())[0].toInt(), workOutStartTime.text.split(":".toRegex())[1].toInt(), true
            )
            timerPickerDialog.show()
        }

        workOutEndTime.setOnClickListener {
            val timerPickerDialog = TimePickerDialog(
                activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    workOutEndTime.text = "%02d:%02d".format(hourOfDay, minute)
                }, workOutEndTime.text.split(":".toRegex())[0].toInt(), workOutEndTime.text.split(":".toRegex())[1].toInt(), true
            )
            timerPickerDialog.show()
        }

        PushDownAnim.setPushDownAnimTo(saveBtn).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            activity?.findViewById<Stepper>(R.id.stepper)?.progress(3)?.addOnCompleteListener {
                if (Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$").matcher(workOutStartTime.text).matches()
                    && Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$").matcher(workOutEndTime.text).matches()
                ) {
                    (activity as AddWorkOutActivity).workOutStartTime = workOutStartTime.text.toString()
                    (activity as AddWorkOutActivity).workOutEndTime = workOutEndTime.text.toString()
                    (activity as AddWorkOutActivity).onStepperProgressedListener.onStepperProgressed()
                } else {
                    Snackbar.make(view, "운동 시간을 입력해 주세요", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        PushDownAnim.setPushDownAnimTo(backBtn).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            view.findNavController().popBackStack()
            activity?.findViewById<Stepper>(R.id.stepper)?.back()
        }
    }
}