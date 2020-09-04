package com.example.coolweather.gson

import com.google.gson.annotations.SerializedName

class Now {
    @SerializedName("tmp")
    var tmp: String? = null
    @SerializedName("cond")
    var more: More? = null
}
class More {
    @SerializedName("txt")
    var info: String? = null
}