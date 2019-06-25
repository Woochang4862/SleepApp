package com.jeongwoochang.sleepapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.YTASMRHolder;
import com.jeongwoochang.sleepapp.util.data.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YTASMRListAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private ArrayList<Item> items;

    public YTASMRListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return (position < items.size()) ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_recyclerview_load_more, parent, false);
            vh = new ProgressViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yt_asmr_list, parent, false);
            vh = new YTASMRHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof YTASMRHolder) {
            Item item = items.get(position);
            Picasso.get().load(item.getSnippet().getThumbnails().getHigh().getUrl()).placeholder(R.drawable.ic_close).into(((YTASMRHolder) holder).thumbnail);
            ((YTASMRHolder) holder).title.setText(item.getSnippet().getTitle());
            ((YTASMRHolder) holder).channel.setText(item.getSnippet().getChannelTitle());
        } else {
            if (limit == position) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size()+1;
    }

    public void clear() {
        items.clear();
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addItems(ArrayList<Item> items) {
        this.items.addAll(items);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        YTASMRHolder.onItemClickListener = onItemClickListener;
    }

    public Item getItem(int position) {
        return items.get(position);
    }
}
