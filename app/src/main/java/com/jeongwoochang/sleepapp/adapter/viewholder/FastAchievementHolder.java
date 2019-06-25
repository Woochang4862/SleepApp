package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.OnItemClickListener;

public class FastAchievementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView day, isSuccess;
    public static OnItemClickListener onItemClickListener;

    public FastAchievementHolder(@NonNull View itemView) {
        super(itemView);
        day = itemView.findViewById(R.id.day);
        isSuccess = itemView.findViewById(R.id.isSuccess);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v, getAdapterPosition());
    }
}
