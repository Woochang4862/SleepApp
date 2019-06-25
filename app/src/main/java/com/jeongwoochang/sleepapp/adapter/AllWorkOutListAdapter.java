package com.jeongwoochang.sleepapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.AllWorkOutHolder;
import com.jeongwoochang.sleepapp.util.data.WorkOut;

import java.util.ArrayList;

public class AllWorkOutListAdapter extends RecyclerView.Adapter<AllWorkOutHolder> {
    private ArrayList<WorkOut> items;
    private Context context;
    private int[] colors = {R.color.work_out_back_ground_color_1, R.color.work_out_back_ground_color_2, R.color.work_out_back_ground_color_3, R.color.work_out_back_ground_color_4, R.color.work_out_back_ground_color_5};
    private String[] dayOfWeek = {"일", "월", "화", "수", "목", "금", "토"};

    public AllWorkOutListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AllWorkOutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_work_out_list, parent, false);
        return new AllWorkOutHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllWorkOutHolder holder, int position) {
        WorkOut item = items.get(position);
        holder.workOutTitle.setText(item.getTitle());
        holder.workOutTime.setText(item.getStartTime() + "~" + item.getEndTime());
        holder.workOutInfoArea.setBackgroundTintList(ContextCompat.getColorStateList(context, colors[position % 5]));
        String dayOfWeek = "";
        for (int i = 0; i < item.getDayOfWeek().size(); i++) {
            if (item.getDayOfWeek().get(i)) dayOfWeek += this.dayOfWeek[i] + " ";
        }
        holder.dayOfWeek.setText(dayOfWeek);
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public void setItems(ArrayList<WorkOut> items) {
        this.items = items;
    }
}
