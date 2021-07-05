package com.yhkim.mymelon

import android.media.MediaMetadataRetriever
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    // MELON
    @GET("melon/list/")
    fun getSongList() : Call<ArrayList<Song>>

}