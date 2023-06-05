package com.poly.downloadtiktokvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.poly.downloadtiktokvideo.api.apiService;
import com.poly.downloadtiktokvideo.model.DownloadTikTok;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 10;
    EditText ed_link;
    Button button,btn_pasted;
//    ImageView imageView;
    BottomNavigationView navigationView;
    ProgressDialog progressDialog;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_link = findViewById(R.id.ed_link);
        button = findViewById(R.id.button);
//        imageView = findViewById(R.id.imageView);
        btn_pasted = findViewById(R.id.button2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading video...");

        loadAds();

        navigationView= findViewById(R.id.bottom_nav_bar);

        //bottom nav
        navigationView.setSelectedItemId(R.id.page_1);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        return true;
                    case R.id.page_2:
                        startActivity(new Intent(MainActivity.this,MainActivity2.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
//        imageView.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tiktokUrl = ed_link.getText().toString().trim();
                Pattern pattern = Pattern.compile("^https://(?:www\\.)?tiktok\\.com/(?:.*\\/)?video/(?:.*)$");
                Matcher matcher = pattern.matcher(tiktokUrl);

                Pattern pattern2 = Pattern.compile("^https://(?:vt\\.)?tiktok\\.com/(?:.*\\/)$");
                Matcher matcher2 = pattern2.matcher(tiktokUrl);
                if (matcher.matches() ||matcher2.matches()) {
                    checkInternet();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong link", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pasted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = clipboardManager.getPrimaryClip();
                ClipData.Item item = clipData.getItemAt(0);
                String data = item.getText().toString().trim();
                ed_link.setText(data);
            }
        });
    }
    boolean connected = false;
    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            progressDialog.show();
            checkQuyen();
            ed_link.setText("");
        }
        else {
            connected = false;
            Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdSize adSize = new AdSize(300, 50);
    }

    private void checkQuyen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String [] permisson = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permisson,REQUEST_PERMISSION_CODE);
            }else {
                getData();
            }
        }else {
            getData();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getData();
            }else {
                Toast.makeText(this, "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getData() {
        String url = ed_link.getText().toString().trim();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService apiService = retrofit.create(apiService.class);
        Call<DownloadTikTok> call = apiService.downloadvideo(url);
        call.enqueue(new Callback<DownloadTikTok>() {
            @Override
            public void onResponse(Call<DownloadTikTok> call, Response<DownloadTikTok> response) {
                if (response.isSuccessful()){
                    DownloadTikTok tikTok = response.body();
                    try {
                        String linkvideo = String.valueOf(response.body().getVideo().get(0));
                        startDownload(linkvideo);

                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Sai link video", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DownloadTikTok> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDownload(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //set mang duoc phep de tai
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Download file");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //xac dich luu file
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/video/" + System.currentTimeMillis() + ".mp4");
        request.setMimeType("video/mp4");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null){
            downloadManager.enqueue(request);
            progressDialog.dismiss();
            Toast.makeText(this, "Download Successful", Toast.LENGTH_SHORT).show();
        }

    }
}