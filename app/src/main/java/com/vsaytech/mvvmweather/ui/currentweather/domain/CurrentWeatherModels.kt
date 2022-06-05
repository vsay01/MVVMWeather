package com.vsaytech.mvvmweather.ui.currentweather.domain

import com.vsaytech.mvvmweather.data.model.ConditionXWS

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */
data class CurrentWeather(
    val name: String,
    val region: String,
    val country: String,
    val currentDayTime: String,
    val temp_c: Double,
    val temp_f: Double,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val wind_mph: Double,
    val wind_kph: Double,
    val humidity: Int,
    val conditionText: String,
    val conditionIcon: String
)

data class CurrentWeatherDailyForecast(
    val day: String,
    val monthDay: String,
    val conditionText: String,
    val conditionIcon: String,
    val maxtemp_c: Double,
    val maxtemp_f: Double,
    val mintemp_c: Double,
    val mintemp_f: Double,
)