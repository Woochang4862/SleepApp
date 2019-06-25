package com.jeongwoochang.sleepapp.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import kotlinx.android.synthetic.main.activity_edit_todolist.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*
import kotlin.collections.ArrayList

class EditTODOListActivity : AppCompatActivity() {

    private lateinit var editTODOListAdapter: EditTODOListAdapter
    private val todoListToRemove: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todolist)

        val d = intent.getSerializableExtra(DBAdapter.TODO_DATE) as DateTime
        date.text = DateTimeFormat.forPattern("yyyy-MM-dd EEEE").withLocale(Locale.US).print(d)
        editTODOList.layoutManager = LinearLayoutManager(this)
        editTODOListAdapter = EditTODOListAdapter()
        editTODOListAdapter.setOnCheckedChangeListener { id, isChecked ->
            val dbAdapter = DBAdapter.getInstance()
            DBAdapter.connect(this)
            dbAdapter.setTODOSuccess(id, isChecked)
            dbAdapter.close()
            if (todoListToRemove.isEmpty()) {
                doneBtn.isEnabled = false
                doneBtn.alpha = 0.5F
            } else {
                doneBtn.isEnabled = true
                doneBtn.alpha = 1F
            }
        }
        doneBtn.setOnClickListener {
            finish()
        }
        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(this)
        editTODOListAdapter.setItems(dbAdapter.getTODO(d))
        dbAdapter.close()
        editTODOList.adapter = editTODOListAdapter
    }
}
