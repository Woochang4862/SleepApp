package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;

public class TodayWorkOutHolder extends RecyclerView.ViewHolder {
    public CheckBox checkBox;
    public CardView workOutInfoArea;
    public TextView workOutTime, workOutTitle;

    public TodayWorkOutHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkbox);
        workOutInfoArea = itemView.findViewById(R.id.workOutInfoArea);
        workOutTime = itemView.findViewById(R.id.workOutTime);
        workOutTitle = itemView.findViewById(R.id.workOutTitle);
    }
}
