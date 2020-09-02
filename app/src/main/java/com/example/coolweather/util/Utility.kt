package com.example.coolweather.util

import android.text.TextUtils
import com.example.coolweather.db.City
import com.example.coolweather.db.County
import com.example.coolweather.db.Province
import org.json.JSONArray
import org.json.JSONException

class Utility {
    companion object {
        fun handleProvinceResponse(data: String?): Boolean {
            if (!TextUtils.isEmpty(data)) {
                try {
                    val provinces = JSONArray(data)
                    for (i in 0..provinces.length()) {
                        val provinceObject = provinces.getJSONObject(i)
                        Province(null, provinceObject.getString("name"), provinceObject.getInt("id"))
                            .save()
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
    }
}