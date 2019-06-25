package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;

public class SubTODOHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public SubTODOHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
    }
}
