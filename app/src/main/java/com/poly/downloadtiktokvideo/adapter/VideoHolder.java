package com.poly.downloadtiktokvideo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.downloadtiktokvideo.R;

public class VideoHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    RelativeLayout click_video;
    public VideoHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_video);
        click_video = itemView.findViewById(R.id.click_video);

    }
}
