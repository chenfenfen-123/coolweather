package com.example.coolweather.gson

import com.google.gson.annotations.SerializedName

class Basic {
    @SerializedName("city")
    var cityName: String? = null
    @SerializedName("id")
    var weatherId: String? = null
    @SerializedName ("update")
    var update: Update? = null
}
class Update {
    @SerializedName("loc")
    var updateName: String? = null
}