package com.vsaytech.mvvmweather.data.repository.currentweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.vsaytech.mvvmweather.data.database.CurrentWeatherDatabase
import com.vsaytech.mvvmweather.data.network.RemoteDataSource
import com.vsaytech.mvvmweather.data.network.WeatherNetwork
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDailyForecastDomainModel
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDomainModel
import com.vsaytech.mvvmweather.data.network.asDatabaseModel
import com.vsaytech.mvvmweather.ui.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.domain.CurrentWeatherDailyForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for fetching current weather from the network
 */
class CurrentWeatherRepository(private val database: CurrentWeatherDatabase) {

    val currentWeather: LiveData<CurrentWeather> = Transformations.map(database.currentWeatherDao.getCurrentWeather()) {
        it.asCurrentWeatherDomainModel()
    }

    val dailyForecastWeatherList: LiveData<List<CurrentWeatherDailyForecast>> = Transformations.map(database.currentWeatherDao.getCurrentWeather()) {
        it.forecast.asCurrentWeatherDailyForecastDomainModel()
    }

    suspend fun getCurrentWeather(location: String) {
        withContext(Dispatchers.IO) {
            Timber.d("current weather is called")
            val currentWeather = RemoteDataSource(WeatherNetwork.currentWeather).getCurrentWeatherByLocation(location)
            database.currentWeatherDao.insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }
}