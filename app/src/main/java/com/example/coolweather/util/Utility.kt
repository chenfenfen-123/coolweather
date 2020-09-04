package com.example.coolweather.util

import android.text.TextUtils
import com.example.coolweather.db.City
import com.example.coolweather.db.County
import com.example.coolweather.db.Province
import com.example.coolweather.gson.Weather
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class Utility {
    companion object {
        fun handleProvinceResponse(data: String?): Boolean {
            if (!TextUtils.isEmpty(data)) {
                try {
                    val provinces = JSONArray(data)
                    for (i in 0..provinces.length()) {
                        val provinceObject = provinces.getJSONObject(i)
                        Province(null, provinceObject.getString("name"), provinceObject.getInt("id"))
                            .save() //保存数据库
                    }
                    return true
                }catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun handleCityResponse(data: String?, provinceId: Int?): Boolean {
            if (!TextUtils.isEmpty(data)) {
                try {
                    val cities = JSONArray(data)
                    for (i in 0..cities.length()) {
                        val cityObject = cities.getJSONObject(i)
                        City(null, cityObject.getString("name"), cityObject.getInt("id"),provinceId)
                            .save()
                    }
                    return true
                }catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun handleCountyResponse(data: String?, cityId: Int?): Boolean {
            if(!TextUtils.isEmpty(data)) {
                try {
                    val counties = JSONArray(data)
                    for (i in 0..counties.length()) {
                        val countyObject = counties.getJSONObject(i)
                        County(null,countyObject.getString("name"), countyObject.getString("weather_id"), cityId)
                            .save()
                    }
                    return true
                }catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun handleWeatherResponse(data: String): Weather? {
            try {
                val jsonObject = JSONObject(data)
                val jsonArray = jsonObject.optJSONArray("HeWeather")
                return Gson().fromJson(jsonArray?.getJSONObject(0).toString(),Weather::class.java)
            }catch (e: Exception) {
                e.printStackTrace()
            }
           return null
        }
    }
}