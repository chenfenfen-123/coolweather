package com.example.coolweather.db

import org.litepal.crud.LitePalSupport

class Province(val id: Int?, val provinceName: String, val provinceCode: Int) : LitePalSupport()