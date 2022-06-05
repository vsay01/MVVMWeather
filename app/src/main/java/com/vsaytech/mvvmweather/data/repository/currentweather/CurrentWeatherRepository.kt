package com.vsaytech.mvvmweather.data.repository.currentweather

import com.vsaytech.mvvmweather.data.model.CurrentWeatherWS
import com.vsaytech.mvvmweather.data.network.RemoteDataSource
import com.vsaytech.mvvmweather.data.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for fetching current weather from the network
 */
class CurrentWeatherRepository {

    suspend fun getCurrentWeather(location: String): CurrentWeatherWS {
        return withContext(Dispatchers.IO) {
            Timber.d("getCurrentWeather is called")
            return@withContext RemoteDataSource(WeatherNetwork.currentWeather).getCurrentWeatherByLocation(location)
        }
    }
}