package com.example.coolweather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import com.example.coolweather.ApiService
import com.example.coolweather.util.Utility
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AutoUpdateService : Service() {

    private val retrofit = Retrofit
        .Builder()
        .baseUrl("http://guolin.tech/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateWeather()
        updateImage()
        val anHour = 8*60*60*100
        val triggerAtTime = SystemClock.elapsedRealtime() + anHour
        val intent = Intent(this,AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this,0,intent,0)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.apply {
            cancel(pi)
            set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather",null)
        if (weatherString != null) {
            val weather = Utility.handleWeatherResponse(weatherString)
            val weatherId = weather?.basic?.weatherId
            apiService.getWeatherInfo(weatherId,"bc0418b57b2d4918819d3974ac1285d9").enqueue(object :
                Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val data = response.body()?.string()
                    val weather1 = data?.let {
                        Utility.handleWeatherResponse(it)
                    }
                    if (weather1 != null && "ok" == weather1.status) {
                        PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit().apply {
                            putString("weather", data)
                            apply()
                        }
                    }
                }

            })
        }
    }

    fun updateImage() {
        apiService.loadImg().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val image = response.body()?.string()
                PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit().apply {
                    putString("bing_pic", image)
                    apply()
                }
            }

        })
    }
}
