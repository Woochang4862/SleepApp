package com.jeongwoochang.sleepapp.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.OnItemClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

public class ShareTODOHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RoundedImageView profileImage, boardImage;
    public TextView username, boardDate, like, title, content;
    public RelativeLayout likeArea;
    public ImageView likeBtn, unlikeBtn;
    public static OnItemClickListener onItemClickListener;

    public ShareTODOHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        title = itemView.findViewById(R.id.title);
        profileImage = itemView.findViewById(R.id.profileImage);
        username = itemView.findViewById(R.id.username);
        boardDate = itemView.findViewById(R.id.boardDate);
        boardImage = itemView.findViewById(R.id.boardImage);
        likeArea = itemView.findViewById(R.id.likeArea);
        like = itemView.findViewById(R.id.like);
        likeBtn = itemView.findViewById(R.id.likeBtn);
        unlikeBtn = itemView.findViewById(R.id.unlikeBtn);
        content = itemView.findViewById(R.id.content);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(v, getAdapterPosition());
    }
}
