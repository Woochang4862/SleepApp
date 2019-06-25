package com.jeongwoochang.sleepapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.MenuItemHolder;
import com.jeongwoochang.sleepapp.util.data.Menu;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuItemHolder> {

    private ArrayList<Menu> items;

    public MenuListAdapter(ArrayList<Menu> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_menu_list, parent, false);
        return new MenuItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
        Menu item = items.get(position);
        holder.textView.setText(item.getTitle());
        holder.imageView.setImageResource(item.getImgRes());
    }

    @Override
    public int getItemCount() {
        if(items == null)return 0;
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        MenuItemHolder.onItemClickListener = onItemClickListener;
    }
}
