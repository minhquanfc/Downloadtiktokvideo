package com.poly.downloadtiktokvideo.api;

import com.poly.downloadtiktokvideo.model.DownloadTikTok;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface apiService {
    @GET("index")
    @Headers(
            {"Accept: application/json",
                    "X-RapidAPI-Key: 950bca1ea5mshe611190d3a9a976p1e8b95jsnda48fcdbe7fc",
                    "X-RapidAPI-Host: tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com"
            }
    )
    Call<DownloadTikTok> downloadvideo(@Query("url") String url);
}
