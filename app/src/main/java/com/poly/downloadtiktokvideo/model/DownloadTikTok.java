package com.poly.downloadtiktokvideo.model;

import java.util.ArrayList;

public class DownloadTikTok {
    public ArrayList<String> video;
    public ArrayList<String> music;
    public ArrayList<String> cover;
    public ArrayList<String> author;


    public DownloadTikTok() {
    }

    public DownloadTikTok(ArrayList<String> video, ArrayList<String> music, ArrayList<String> cover, ArrayList<String> author) {
        this.video = video;
        this.music = music;
        this.cover = cover;
        this.author = author;
    }

    public ArrayList<String> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<String> video) {
        this.video = video;
    }

    public ArrayList<String> getMusic() {
        return music;
    }

    public void setMusic(ArrayList<String> music) {
        this.music = music;
    }

    public ArrayList<String> getCover() {
        return cover;
    }

    public void setCover(ArrayList<String> cover) {
        this.cover = cover;
    }

    public ArrayList<String> getAuthor() {
        return author;
    }

    public void setAuthor(ArrayList<String> author) {
        this.author = author;
    }
}
