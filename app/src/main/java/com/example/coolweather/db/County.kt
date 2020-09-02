package com.example.coolweather.db

import org.litepal.crud.LitePalSupport

class County(val id: Int?, val countyName: String, val weatherId: String, val cityId: Int?): LitePalSupport()