package com.vsaytech.mvvmweather.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vsaytech.mvvmweather.databinding.ActivityMyWeatherBinding

class MyWeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}