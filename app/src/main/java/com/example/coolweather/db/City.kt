package com.example.coolweather.db

import org.litepal.crud.LitePalSupport

class City(val id: Int?, val cityName: String, val cityCode: Int, val provinceId: Int?): LitePalSupport()