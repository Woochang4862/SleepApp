package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.OnItemClickListener;

public class YTASMRHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView thumbnail;
    public TextView title, channel;
    public static OnItemClickListener onItemClickListener;

    public YTASMRHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        channel = itemView.findViewById(R.id.channel);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v, getAdapterPosition());
    }
}
