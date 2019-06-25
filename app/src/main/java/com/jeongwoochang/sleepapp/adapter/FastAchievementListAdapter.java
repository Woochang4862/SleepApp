package com.jeongwoochang.sleepapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.FastAchievementHolder;
import com.jeongwoochang.sleepapp.util.data.FastAchievement;

import java.util.ArrayList;

public class FastAchievementListAdapter extends RecyclerView.Adapter<FastAchievementHolder> {

    private ArrayList<FastAchievement> items;
    private Context context;

    public FastAchievementListAdapter(Context context, ArrayList<FastAchievement> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public FastAchievementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fast_achievement_list, parent, false);
        return new FastAchievementHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FastAchievementHolder holder, int position) {
        FastAchievement item = items.get(position);
        holder.day.setText(item.getDay());
        holder.isSuccess.setTextColor(context.getResources().getColor(item.isSuccess() ? R.color.success_color : R.color.failed_color));
        holder.isSuccess.setText(item.isSuccess() ? "SUCCESS" : "FAILED");
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public void addItem(FastAchievement item) {
        items.add(item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        FastAchievementHolder.onItemClickListener = onItemClickListener;
    }
}
