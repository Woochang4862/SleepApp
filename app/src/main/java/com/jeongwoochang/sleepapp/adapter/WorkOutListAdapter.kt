package com.jeongwoochang.sleepapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.adapter.viewholder.WorkOutHolder
import com.jeongwoochang.sleepapp.util.data.WorkOut

class WorkOutListAdapter(context: Context) : RecyclerView.Adapter<WorkOutHolder>() {

    private lateinit var items: ArrayList<WorkOut>
    private val colors = intArrayOf(
        R.color.work_out_back_ground_color_1,
        R.color.work_out_back_ground_color_2,
        R.color.work_out_back_ground_color_3,
        R.color.work_out_back_ground_color_4,
        R.color.work_out_back_ground_color_5
    )
    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOutHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_work_out_list, parent, false)
        return WorkOutHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WorkOutHolder, position: Int) {
        val item = items[position]
        holder.workOutTitle.text = item.title
        holder.workOutTime.text = item.startTime + "~" + item.endTime
        holder.workOutInfoArea.backgroundTintList = ContextCompat.getColorStateList(context, colors[position % 5])
    }

    public fun setItems(items: ArrayList<WorkOut>){
        this.items=items
    }
}
