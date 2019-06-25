package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.OnItemClickListener;

public class TODOHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static OnItemClickListener onItemClickListener;

    public RecyclerView todoList;
    public TODOHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        todoList = itemView.findViewById(R.id.todoList);
    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener != null)
            onItemClickListener.onItemClick(v, getAdapterPosition());
    }
}
