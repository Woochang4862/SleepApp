package com.jeongwoochang.sleepapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.EditTODOHolder;
import com.jeongwoochang.sleepapp.util.data.TODO;

import java.util.ArrayList;

public class EditTODOListAdapter extends RecyclerView.Adapter<EditTODOHolder> {
    private ArrayList<TODO> items;
    private OnCheckedChangeListener onCheckedChangeListener;

    @NonNull
    @Override
    public EditTODOHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_todo_list, parent, false);
        return new EditTODOHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EditTODOHolder holder, int position) {
        final TODO item = items.get(position);
        holder.name.setText(item.getName());
        holder.success.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.name.setTextColor(Color.parseColor("#ff5c82"));
                } else {
                    holder.name.setTextColor(Color.parseColor("#a5a5a5"));
                }
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChange(item.get_id(), isChecked);
            }
        });
        holder.success.setChecked(item.isSuccess());
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public void setItems(ArrayList<TODO> items) {
        this.items = items;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int id, boolean isChecked);
    }
}
