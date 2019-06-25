package com.jeongwoochang.sleepapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.data.WorkOut
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import kotlinx.android.synthetic.main.activity_add_work_out.*

class AddWorkOutActivity : AppCompatActivity(), OnStepperProgressedListener {
    override fun onStepperProgressed() {
        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(this)
        dbAdapter.addWorkOut(WorkOut(nameOfWorkOut, dayOfWeekClickedList, workOutStartTime, workOutEndTime))
        dbAdapter.setTodayWorkOut()
        dbAdapter.close()
        finish()
    }

    lateinit var onStepperProgressedListener: OnStepperProgressedListener

    var nameOfWorkOut: String = ""
    var dayOfWeekClickedList: ArrayList<Boolean> = ArrayList()
    var workOutStartTime: String = ""
    var workOutEndTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work_out)

        onStepperProgressedListener = this
    }

    override fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()
}

interface OnStepperProgressedListener {
    fun onStepperProgressed()
}
