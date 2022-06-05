package com.vsaytech.mvvmweather.data.model

data class CurrentWeatherWS(
    val current: CurrentWS,
    val forecast: ForecastWS,
    val location: LocationWS
)