package com.vsaytech.mvvmweather.data.network

import com.vsaytech.mvvmweather.data.model.CurrentWeatherWS
import com.vsaytech.mvvmweather.data.model.ForecastWS
import com.vsaytech.mvvmweather.extensions.ifNull
import com.vsaytech.mvvmweather.ui.currentweather.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.currentweather.domain.CurrentWeatherDailyForecast

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * Convert Network results to CurrentWeather domain object
 */
fun CurrentWeatherWS.asCurrentWeatherDomainModel(): CurrentWeather {
    return CurrentWeather(
        name = location.name.ifNull(),
        region = location.region.ifNull(),
        country = location.country.ifNull(),
        currentDayTime = location.localtime.ifNull(),
        temp_c = current.temp_c.ifNull(),
        temp_f = current.temp_f.ifNull(),
        feelslike_c = current.feelslike_c.ifNull(),
        feelslike_f = current.feelslike_f.ifNull(),
        wind_mph = current.wind_mph.ifNull(),
        wind_kph = current.wind_kph.ifNull(),
        humidity = current.humidity.ifNull(),
        conditionText = current.condition?.text.ifNull(),
        conditionIcon = "https://" + current.condition?.icon.ifNull().removePrefix("//")
    )
}

/**
 * Convert Network results to CurrentWeatherDailyForecast domain object
 */
fun ForecastWS.asCurrentWeatherDailyForecastDomainModel(): List<CurrentWeatherDailyForecast> {
    return forecastday.map {
        CurrentWeatherDailyForecast(
            day = it.date,
            monthDay = it.date,
            conditionText = it.day.condition.text.ifNull(),
            conditionIcon = "https://" + it.day.condition.icon.ifNull().removePrefix("//"),
            maxtemp_c = it.day.maxtemp_c,
            maxtemp_f = it.day.maxtemp_f,
            mintemp_c = it.day.maxtemp_c,
            mintemp_f = it.day.maxtemp_f
        )
    }
}