package com.example.coolweather

import com.example.coolweather.gson.Weather
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather?")
    fun getWeatherInfo(@Query("cityid")weatherId: String?, @Query("key")key: String): Call<ResponseBody>

}