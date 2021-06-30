package com.yhkim.myyoutube

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    // Youtube
    @GET("youtube/list/")
    fun getYouTubeList() : Call<ArrayList<Youtube>>

}