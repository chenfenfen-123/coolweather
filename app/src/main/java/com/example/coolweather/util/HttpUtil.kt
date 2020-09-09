package com.example.coolweather.util

import android.util.Log
import com.example.coolweather.ApiService
import com.example.coolweather.gson.Weather
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpUtil {
    companion object {
        private const val TAG = "HttpUtil"
        fun sendOkHttpRequest (url: String, callback: okhttp3.Callback) {
            val okHttpClient by lazy {
                OkHttpClient.Builder()
                    .connectTimeout(6000L, TimeUnit.MILLISECONDS)
                    .readTimeout(6000L, TimeUnit.MILLISECONDS)
                    .writeTimeout(6000L, TimeUnit.MILLISECONDS)
                    .build()
            }
            val request = Request.Builder()
                .url(url)
                .method("GET", null)
                .build()
            okHttpClient.newCall(request).enqueue(callback)
            }
        }
    }
