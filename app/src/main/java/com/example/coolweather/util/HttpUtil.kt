package com.example.coolweather.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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