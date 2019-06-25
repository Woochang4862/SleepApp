package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.OnItemClickListener;

public class NapAlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView napDayOfWeek, napTime;
    public SwitchMaterial napAlarmActive;
    public static OnItemClickListener onItemClickListener;

    public NapAlarmHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        napDayOfWeek = itemView.findViewById(R.id.napDayOfWeek);
        napTime = itemView.findViewById(R.id.napTime);
        napAlarmActive = itemView.findViewById(R.id.napAlarmActive);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v, getAdapterPosition());
    }
}
