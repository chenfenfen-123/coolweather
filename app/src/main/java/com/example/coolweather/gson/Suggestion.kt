package com.example.coolweather.gson

import com.google.gson.annotations.SerializedName

class Suggestion {
    @SerializedName("comf")
    var comfort: Comfort? = null
    @SerializedName("cw")
    var carWash: CarWash? = null
    @SerializedName("sport")
    var sport: Sport? = null
}

class Sport {
    @SerializedName("txt")
    var info: String? = null
}

class CarWash {
    @SerializedName("txt")
    var info: String? = null
}

class Comfort {
    @SerializedName("txt")
    var info: String? = null
}
