package com.vsaytech.mvvmweather.data.repository.currentweather

import com.vsaytech.mvvmweather.BuildConfig
import com.vsaytech.mvvmweather.data.database.CurrentWeatherDB
import com.vsaytech.mvvmweather.data.database.CurrentWeatherDatabase
import com.vsaytech.mvvmweather.data.network.WeatherService
import com.vsaytech.mvvmweather.data.network.asDatabaseModel
import com.vsaytech.mvvmweather.ui.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.uistate.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository for fetching current weather from the network
 */
class CurrentWeatherRepository @Inject constructor(
    private val database: CurrentWeatherDatabase,
    private val weatherService: WeatherService
) {

    //Use this if you want to return Flow to ViewModel
    val currentWeather: Flow<CurrentWeatherDB> = database.currentWeatherDao.getCurrentWeather()

    suspend fun getCurrentWeather(location: String) {
        withContext(Dispatchers.IO) {
            Timber.d("current weather is called")
            val currentWeather = weatherService.getCurrentWeatherByLocation(BuildConfig.WEATHER_API_KEY, location)
            database.currentWeatherDao.insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }
}