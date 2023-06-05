package com.poly.downloadtiktokvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poly.downloadtiktokvideo.PlayVideoActivity;
import com.poly.downloadtiktokvideo.R;
import com.poly.downloadtiktokvideo.model.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    Context context;
    List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = videoList.get(position);
        if (video == null){
            return;
        }
        Glide.with(context).load(video.getThumb()).into(holder.imageView);
        holder.click_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("path_video",video.getPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
