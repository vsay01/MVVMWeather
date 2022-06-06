package com.vsaytech.mvvmweather.ui.currentweather

import com.vsaytech.mvvmweather.ui.domain.CurrentWeatherDailyForecast

interface DailyForecastClickListener {
    fun onDailyForecastClicked(currentWeatherDailyForecast: CurrentWeatherDailyForecast)
}
