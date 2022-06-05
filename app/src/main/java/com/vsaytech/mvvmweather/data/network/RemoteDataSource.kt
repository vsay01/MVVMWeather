package com.vsaytech.mvvmweather.data.network

import com.vsaytech.mvvmweather.BuildConfig

class RemoteDataSource constructor(private val weatherService: WeatherService) {
    suspend fun getCurrentWeatherByLocation(location: String) =
        weatherService.getCurrentWeatherByLocation(BuildConfig.WEATHER_API_KEY, location)
}