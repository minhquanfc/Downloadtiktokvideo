package com.poly.downloadtiktokvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.poly.downloadtiktokvideo.adapter.VideoAdapter;
import com.poly.downloadtiktokvideo.model.Video;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView navigationView;
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    List<Video> videoList;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        navigationView= findViewById(R.id.bottom_nav_bar_download);
        recyclerView= findViewById(R.id.rcview_video);
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this,videoList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        checkPermission();
        loadAds();


        //bottom nav
        navigationView.setSelectedItemId(R.id.page_2);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        startActivity(new Intent(MainActivity2.this,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_2:
                        return true;
                }
                return false;
            }
        });
    }
    private void loadAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView_download);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdSize adSize = new AdSize(300, 50);
    }
    public  void checkPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getAllVideo();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity2.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void getAllVideo() {
        Uri uri;
        Cursor cursor;
        int columnIndexData,thumb;
        String absolutePathOfImage = null;
        String thumbnail = null;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy+" DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        thumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()){
            absolutePathOfImage = cursor.getString(columnIndexData);
            thumbnail = cursor.getString(thumb);

            Video video = new Video();
            video.setPath(absolutePathOfImage);
            video.setThumb(thumbnail);

            videoList.add(video);
        }
        videoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(videoAdapter);
    }
}