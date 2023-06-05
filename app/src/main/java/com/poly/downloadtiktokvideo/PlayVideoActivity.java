package com.poly.downloadtiktokvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class PlayVideoActivity extends AppCompatActivity {

    Toolbar toolbar;
    ExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        toolbar= findViewById(R.id.toolbar_xemvideo);
        PlayerView playerView = findViewById(R.id.video_player);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String path = getIntent().getStringExtra("path_video");
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(path);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            player.stop();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        player.stop();
        super.onBackPressed();
    }
}