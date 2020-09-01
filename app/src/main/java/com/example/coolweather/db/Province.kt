package com.example.coolweather.db

import org.litepal.crud.LitePalSupport

class Province(val id: Int, val countyName: String, val weatherId: String, val cityId: Int) : LitePalSupport()