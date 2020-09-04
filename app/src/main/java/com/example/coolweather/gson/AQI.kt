package com.example.coolweather.gson

import com.google.gson.annotations.SerializedName

class AQI {
    @SerializedName("city")
    var city: AQICity? = null
}
class AQICity {
    @SerializedName("aqi")
    var aqi: String? = null
    @SerializedName("pm25")
    var pm25: String? = null
}