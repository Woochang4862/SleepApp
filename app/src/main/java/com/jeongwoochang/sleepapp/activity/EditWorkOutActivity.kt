package com.jeongwoochang.sleepapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.adapter.AllWorkOutListAdapter
import com.jeongwoochang.sleepapp.adapter.EditWorkOutListAdapter
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import kotlinx.android.synthetic.main.activity_edit_work_out.*
import timber.log.Timber

class EditWorkOutActivity : AppCompatActivity() {

    private val itemsToRemove: ArrayList<Int> = ArrayList()
    private lateinit var allWorkOutListAdapter: AllWorkOutListAdapter
    private lateinit var editWorkOutListAdapter: EditWorkOutListAdapter
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_work_out)

        workOutList.isFocusableInTouchMode = false
        workOutList.layoutManager = LinearLayoutManager(this)
        allWorkOutListAdapter = AllWorkOutListAdapter(this)
        editWorkOutListAdapter = EditWorkOutListAdapter(this)
        editWorkOutListAdapter.setOnCheckedChangeListener { id, isChecked ->
            if (isChecked) {
                itemsToRemove.add(id)
            } else {
                itemsToRemove.remove(id)
            }
        }
        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(this)
        allWorkOutListAdapter.setItems(dbAdapter.workOut)
        dbAdapter.close()
        workOutList.adapter = allWorkOutListAdapter

        backBtn.setOnClickListener {
            finish()
        }

        editBtn.setOnClickListener {
            editMode(true)
        }

        doneBtn.setOnClickListener {
            val dbAdapter = DBAdapter.getInstance()
            DBAdapter.connect(this)
            itemsToRemove.forEach {
                dbAdapter.removeWorkOut(it)
            }
            dbAdapter.setTodayWorkOut()
            editMode(false)
            dbAdapter.close()
        }

        deleteBtn.setOnClickListener {
            Timber.d("list of editWorkOutListAdapter = ${editWorkOutListAdapter.items}")
            editWorkOutListAdapter.removeItems(itemsToRemove)
            editWorkOutListAdapter.notifyDataSetChanged()
        }
    }

    private fun editMode(b: Boolean) {
        isEditMode = b
        if (b) {
            editBtn.visibility = View.GONE
            backBtn.visibility = View.GONE
            deleteBtn.visibility = View.VISIBLE
            doneBtn.visibility = View.VISIBLE
            val dbAdapter = DBAdapter.getInstance()
            DBAdapter.connect(this)
            editWorkOutListAdapter.items = dbAdapter.workOut
            dbAdapter.close()
            workOutList.adapter = editWorkOutListAdapter
            editWorkOutListAdapter.notifyDataSetChanged()
        } else {
            editBtn.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.GONE
            doneBtn.visibility = View.GONE
            val dbAdapter = DBAdapter.getInstance()
            DBAdapter.connect(this)
            allWorkOutListAdapter.setItems(dbAdapter.workOut)
            dbAdapter.close()
            workOutList.adapter = allWorkOutListAdapter
            allWorkOutListAdapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        if (isEditMode) {
            editMode(false)
            itemsToRemove.clear()
        } else {
            super.onBackPressed()
        }
    }
}
