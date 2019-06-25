package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;

public class WorkOutHolder extends RecyclerView.ViewHolder {
    public CardView workOutInfoArea;
    public TextView workOutTime, workOutTitle;

    public WorkOutHolder(@NonNull View itemView) {
        super(itemView);
        workOutInfoArea = itemView.findViewById(R.id.workOutInfoArea);
        workOutTime = itemView.findViewById(R.id.workOutTime);
        workOutTitle = itemView.findViewById(R.id.workOutTitle);
    }
}
