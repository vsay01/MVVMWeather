package com.vsaytech.mvvmweather.data.repository.currentweather

import com.vsaytech.mvvmweather.data.database.CurrentWeatherDB
import com.vsaytech.mvvmweather.data.database.CurrentWeatherDatabase
import com.vsaytech.mvvmweather.data.network.RemoteDataSource
import com.vsaytech.mvvmweather.data.network.WeatherNetwork
import com.vsaytech.mvvmweather.data.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for fetching current weather from the network
 */
class CurrentWeatherRepository(private val database: CurrentWeatherDatabase) {

    //Use this if you want to return LiveData to ViewModel
    /*val currentWeather: LiveData<CurrentWeather> = Transformations.map(database.currentWeatherDao.getCurrentWeather().asLiveData()) {
        it.asCurrentWeatherDomainModel()
    }

    val dailyForecastWeatherList: LiveData<List<CurrentWeatherDailyForecast>> = Transformations.map(database.currentWeatherDao.getCurrentWeather().asLiveData()) {
        it.forecast.asCurrentWeatherDailyForecastDomainModel()
    }*/

    //Use this if you want to return Flow to ViewModel
    val currentWeather: Flow<CurrentWeatherDB> = database.currentWeatherDao.getCurrentWeather()

    suspend fun getCurrentWeather(location: String) {
        withContext(Dispatchers.IO) {
            Timber.d("current weather is called")
            val currentWeather = RemoteDataSource(WeatherNetwork.currentWeather).getCurrentWeatherByLocation(location)
            database.currentWeatherDao.insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }
}