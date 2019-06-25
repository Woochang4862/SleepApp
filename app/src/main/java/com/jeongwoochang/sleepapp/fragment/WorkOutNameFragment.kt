package com.jeongwoochang.sleepapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_work_out_name.*

class WorkOutNameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_work_out_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PushDownAnim.setPushDownAnimTo(backBtn).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            activity?.findViewById<Stepper>(R.id.stepper)?.stop()
            activity?.finish()
        }

        PushDownAnim.setPushDownAnimTo(next).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            if (workOutName.text.toString() == "") {
                Snackbar.make(view, "운동 이름을 설정해 주십시오", Snackbar.LENGTH_SHORT).show()
            } else {
                view.findNavController().navigate(R.id.fragmentWorkOutNameToFrequency)
                activity?.findViewById<Stepper>(R.id.stepper)?.forward()
                (activity as AddWorkOutActivity).nameOfWorkOut = workOutName.text.toString()
            }
        }
    }
}
