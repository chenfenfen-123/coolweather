package com.example.coolweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val pref = PreferenceManager.getDefaultSharedPreferences(this)
//        if (pref != null) {
//            startActivity(Intent(this,WeatherActivity::class.java))
//            finish()
//        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,ChooseAreaFragment.newInstance("test1","test2"))
            commitAllowingStateLoss()
        }
    }
}