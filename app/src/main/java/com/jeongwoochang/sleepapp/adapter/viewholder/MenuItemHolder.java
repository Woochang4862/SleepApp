package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.OnItemClickListener;

public class MenuItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textView;
    public ImageView imageView;
    public static OnItemClickListener onItemClickListener;

    public MenuItemHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.menuImage);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener != null)
            onItemClickListener.onItemClick(v,getAdapterPosition());
    }
}
