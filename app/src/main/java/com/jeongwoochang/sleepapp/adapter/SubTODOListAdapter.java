package com.jeongwoochang.sleepapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.SubTODOHolder;

import java.util.ArrayList;

public class SubTODOListAdapter extends RecyclerView.Adapter<SubTODOHolder> {
    public ArrayList<String> items;
    @NonNull
    @Override
    public SubTODOHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_todo_lisst, parent, false);
        return new SubTODOHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTODOHolder holder, int position) {
        holder.name.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        if(items == null)return 0;
        return items.size();
    }
}
