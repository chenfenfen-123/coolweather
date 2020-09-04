package com.example.coolweather.gson

import com.google.gson.annotations.SerializedName

class Forecast {
    @SerializedName("date")
    var date: String? = null
    @SerializedName("cond")
    var cond: Cond? = null
    @SerializedName("tmp")
    var temperature: Temperature? = null
}

class Cond {
    @SerializedName("txt_d")
    var txt_d: String? = null
}

class Temperature {
    @SerializedName("max")
    var max: String? = null
    @SerializedName("min")
    var min: String? = null
}
