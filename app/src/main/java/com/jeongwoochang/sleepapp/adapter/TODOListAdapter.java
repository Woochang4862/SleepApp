package com.jeongwoochang.sleepapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.SubTODOHolder;
import com.jeongwoochang.sleepapp.util.data.TODO;

import java.util.ArrayList;

public class TODOListAdapter extends RecyclerView.Adapter<SubTODOHolder> {

    private ArrayList<TODO> items;

    @NonNull
    @Override
    public SubTODOHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_todo_lisst, parent, false);
        return new SubTODOHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTODOHolder holder, int position) {
        TODO item = items.get(position);
        holder.name.setText(item.getName());
        if (item.isSuccess()) {
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public void setItems(ArrayList<TODO> items) {
        this.items = items;
    }
}
