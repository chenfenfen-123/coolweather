package com.example.coolweather.model


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coolweather.ApiService
import com.example.coolweather.gson.Weather
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val weatherInfo = MutableLiveData<Weather>()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl("http://guolin.tech/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)

    init {




    }
}