package com.example.coolweather.gson

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("basic")
    var basic: Basic? = null
    @SerializedName("aqi")
    var aqi: AQI? = null
    @SerializedName("now")
    var now: Now? = null
    @SerializedName("suggestion")
    var suggestion: Suggestion?  = null
    @SerializedName("daily_forecast")
    var forecastList: List<Forecast>? = null
}