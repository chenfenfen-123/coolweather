package com.example.coolweather

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.Layout
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.bumptech.glide.util.Util
import com.example.coolweather.gson.Weather
import com.example.coolweather.util.HttpUtil
import com.example.coolweather.util.Utility
import kotlinx.android.synthetic.main.forecast_item.*
import okhttp3.Call
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Callback
import java.io.IOException

class WeatherActivity : AppCompatActivity() {
    private val mHandler = Handler(Looper.myLooper()!!)
    private lateinit var weatherLayout: ScrollView
    private lateinit var titleCity: TextView
    private lateinit var degreeText: TextView
    private lateinit var weatherInfoText: TextView
    private lateinit var aqiText: TextView
    private lateinit var pm25Text: TextView
    private lateinit var comfortText: TextView
    private lateinit var carWashText: TextView
    private lateinit var titleUpdateTime: TextView
    private lateinit var forecastLayout: LinearLayout
    private lateinit var sportText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        weatherLayout = findViewById(R.id.weather_layout)
        titleCity = findViewById(R.id.title_city)
        titleUpdateTime = findViewById(R.id.title_update_time)
        degreeText = findViewById(R.id.degree_text)
        weatherInfoText = findViewById(R.id.weather_info_text)
        forecastLayout = findViewById(R.id.forecast_layout)
        aqiText = findViewById(R.id.aqi_text)
        pm25Text = findViewById(R.id.pm25_text)
        comfortText = findViewById(R.id.comfort_text)
        carWashText = findViewById(R.id.car_wash_text)
        sportText = findViewById(R.id.sport_text)
        if (!TextUtils.isEmpty(getData("DATA"))){
            showWeatherInfo(Utility.handleWeatherResponse(getData(DATA)))
        }
        else {
            weatherLayout.visibility = View.INVISIBLE
            requestWeather(intent.getStringExtra("weather_id"))
        }
    }

    /*
    处理并展示Weather实体类中的数据
     */
    @SuppressLint("SetTextI18n")
    private fun showWeatherInfo(weather: Weather?) {
        titleCity.text = weather?.basic?.cityName ?: ""
        titleUpdateTime.text = weather?.basic?.update?.updateName?.split(" ")?.get(1)
        degreeText.text = weather?.now?.tmp+"℃" ?: ""
        weatherInfoText.text = weather?.now?.more?.info ?: ""
        forecastLayout.removeAllViews()
        for (forecast in weather?.forecastList!!) {
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            view.findViewById<TextView>(R.id.date_text).apply {
                text = forecast.date
            }
            view.findViewById<TextView>(R.id.info_text).apply {
                text = forecast.cond?.txt_d
            }
            view.findViewById<TextView>(R.id.max_text).apply {
                text = forecast.temperature?.max
            }
            view.findViewById<TextView>(R.id.min_text).apply {
                text = forecast.temperature?.min
            }
            forecastLayout.addView(view)
        }
        aqiText.text = weather.aqi?.city?.aqi
        pm25Text.text = weather.aqi?.city?.pm25
        comfortText.text = "舒适度"+ weather.suggestion?.comfort?.info
        carWashText.text = "洗车指数"+ weather.suggestion?.carWash?.info
        sportText.text = "运动建议"+ weather.suggestion?.sport?.info
        weatherLayout.visibility = View.VISIBLE
    }

    private fun requestWeather(weatherId: String?) {
        val key = "bc0418b57b2d4918819d3974ac1285d9"
        HttpUtil.requestWeather(weatherId, key, object : Callback<ResponseBody> {
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                mHandler.post {
                    Utility.handleWeatherResponse(getData(DATA))
                    Toast.makeText(this@WeatherActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                val data = response.body()?.string()?: ""
                val weather = Utility.handleWeatherResponse(data)
                mHandler.post {
                    if (response.isSuccessful) {
                        saveData(data)
                        showWeatherInfo(weather)
                    } else {
                        Toast.makeText(this@WeatherActivity, "获取天气信息失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
//        val url = "http://guolin.tech/api/weather?cityid=$weatherId&key=bc0418b57b2d4918819d3974ac1285d9"
//        HttpUtil.sendOkHttpRequest(url, object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                mHandler.post {
//                    Utility.handleWeatherResponse(getData(DATA))
//                    Toast.makeText(this@WeatherActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val data = response.body?.string() ?: ""
//                val weather = Utility.handleWeatherResponse(data)
//                mHandler.post {
//                    if (response.isSuccessful) {
//                        saveData(data)
//                        showWeatherInfo(weather)
//                    }
//                    else {
//                        Toast.makeText(this@WeatherActivity,"获取天气信息失败",Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//        })
    }

    private fun saveData(jsonString: String) {
        getPreferences(Context.MODE_PRIVATE)?.apply {
            edit(true) {
                putString(DATA, jsonString)
            }
        }
    }

    private fun getData(key: String): String {
        return getPreferences(Context.MODE_PRIVATE)?.getString(key, "") ?: ""
    }

    companion object {
        private const val DATA = "weatherInfo"
    }
}