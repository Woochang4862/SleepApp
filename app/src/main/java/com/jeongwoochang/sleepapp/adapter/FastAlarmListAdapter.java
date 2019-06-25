package com.jeongwoochang.sleepapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.NapAlarmHolder;
import com.jeongwoochang.sleepapp.util.data.FastAlarm;

import java.util.ArrayList;

public class FastAlarmListAdapter extends RecyclerView.Adapter<NapAlarmHolder> {

    private ArrayList<FastAlarm> items;

    public FastAlarmListAdapter(ArrayList<FastAlarm> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public NapAlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fast_alarm_list, parent, false);
        return new NapAlarmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NapAlarmHolder holder, int position) {
        FastAlarm item = items.get(position);
        StringBuilder dayOfWeekStr = new StringBuilder();
        for (Integer dayOfWeek : item.getDayOfWeek()) {
            dayOfWeekStr.append(dayOfWeek).append(" ");
        }
        holder.napDayOfWeek.setText(dayOfWeekStr.toString());
        holder.napTime.setText(item.getNapStartTime() + "~" + item.getNapEndTime());
        holder.napAlarmActive.setChecked(item.isActive());
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    public void addItem(FastAlarm item){
        items.add(item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        NapAlarmHolder.onItemClickListener = onItemClickListener;
    }
}
