package com.yhkim.myyoutube

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {

    lateinit var service : RetrofitService

    // 액티비티보다 먼저 호출되는 onCreate이므로 여기서 설정(ex : Retrofit)하면 다른 액티비티에서 가져다 쓸 수 있다.
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        createRetrofit()

        //chrome://inspect/#devices

    }

    fun createRetrofit() {
        val header = Interceptor { // 헤더 생성, Interceptor = > 통신 가로채서 잡아두기
            val original = it.request()
            if (checkIsLogin()) { // 로그인을 했으면
                getUserToken()?.let { token -> // null이 아닌 경우면
                    val request = original.newBuilder() // 원래 네트워크로 나갈 통신을 잡아서 개조하기
                        .header("Authorization", "token " + token) // 헤더 달아주기, token 한칸 무조건 띄어주기
                        .build()
                    it.proceed(request) // 개조한 거 넣어주기
                }
            } else {
                it.proceed(original) // 개조한 거 넣어주기
            }
        }

        val client = OkHttpClient.Builder() // 클라이언트 만들기
            .addInterceptor(header) // 위에서 만든 헤더
            .addNetworkInterceptor(StethoInterceptor()) // StethoInterceptor가 통신을 낚아채서 화면에 보여준다.
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // 위에서 만든 client를 등록(header까지 포함)
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }

    // 로그인 했는지 체크하는 함수(sharedPreference에서 토큰 값이 있냐 없냐?)
    fun checkIsLogin() : Boolean {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")
        if (token != "null") return true
        else return false

    }

    fun getUserToken() : String? {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("login_sp", "null")
        if (token == "null") return null
        else return token

    }
}


// 페이스북에서 만든 네트워크 통신 라이브러리 => stetho