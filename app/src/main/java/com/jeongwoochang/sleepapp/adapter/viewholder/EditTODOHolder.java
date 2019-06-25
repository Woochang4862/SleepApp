package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;

public class EditTODOHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CheckBox success;
    public TextView name;

    public EditTODOHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        success = itemView.findViewById(R.id.success);
        name = itemView.findViewById(R.id.name);
    }

    @Override
    public void onClick(View v) {
        success.setChecked(!success.isChecked());
    }
}
