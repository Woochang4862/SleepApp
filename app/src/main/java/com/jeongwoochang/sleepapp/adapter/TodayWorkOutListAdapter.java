package com.jeongwoochang.sleepapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.TodayWorkOutHolder;
import com.jeongwoochang.sleepapp.util.data.TodayWorkOut;

import java.util.ArrayList;

public class TodayWorkOutListAdapter extends RecyclerView.Adapter<TodayWorkOutHolder> {
    private ArrayList<TodayWorkOut> items;
    private OnCheckedChangeListener onCheckedChangeListener;
    private int[] colors = {R.color.work_out_back_ground_color_1, R.color.work_out_back_ground_color_2, R.color.work_out_back_ground_color_3, R.color.work_out_back_ground_color_4, R.color.work_out_back_ground_color_5};
    private Context context;

    public TodayWorkOutListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TodayWorkOutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today_work_out_list, parent, false);
        return new TodayWorkOutHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodayWorkOutHolder holder, int position) {
        final TodayWorkOut item = items.get(position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.workOutTitle.setPaintFlags(holder.workOutTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.workOutTime.setPaintFlags(holder.workOutTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.workOutTitle.setPaintFlags(holder.workOutTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.workOutTime.setPaintFlags(holder.workOutTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChangeListener(item.get_id(), isChecked);
            }
        });
        holder.workOutTitle.setText(item.getTitle());
        holder.workOutTime.setText(item.getStartTime() + "~" + item.getEndTime());
        holder.workOutInfoArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });
        holder.workOutInfoArea.setBackgroundTintList(ContextCompat.getColorStateList(context, colors[position % 5]));
        holder.checkBox.setChecked(item.isSuccess());
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public void setItems(ArrayList<TodayWorkOut> items) {
        this.items = items;
    }

    public void addItems(TodayWorkOut item) {
        items.add(item);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChangeListener(int id, boolean isSuccess);
    }
}
