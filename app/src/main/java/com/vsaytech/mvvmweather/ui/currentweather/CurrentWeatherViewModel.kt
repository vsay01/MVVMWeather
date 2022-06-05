package com.vsaytech.mvvmweather.ui.currentweather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDailyForecastDomainModel
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDomainModel
import com.vsaytech.mvvmweather.data.repository.currentweather.CurrentWeatherRepository
import com.vsaytech.mvvmweather.ui.currentweather.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.currentweather.domain.CurrentWeatherDailyForecast
import kotlinx.coroutines.launch
import java.io.IOException

class CurrentWeatherViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val currentWeatherRepository = CurrentWeatherRepository()

    /**
     * A currentWeather displayed on the screen.
     */
    private val _currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val currentWeather: LiveData<CurrentWeather>
        get() = _currentWeather

    private val _currentWeatherDailyForecastList: MutableLiveData<List<CurrentWeatherDailyForecast>> = MutableLiveData()
    val currentWeatherDailyForecastList: LiveData<List<CurrentWeatherDailyForecast>>
        get() = _currentWeatherDailyForecastList

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /*init {
        refreshCurrentWeatherFromRepository("North Aurora")
    }*/

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun refreshCurrentWeatherFromRepository(location: String) {
        viewModelScope.launch {
            try {
                val currentWeatherWS = currentWeatherRepository.getCurrentWeather(location)
                _currentWeather.value = currentWeatherWS.asCurrentWeatherDomainModel()
                _currentWeatherDailyForecastList.value = currentWeatherWS.forecast.asCurrentWeatherDailyForecastDomainModel()

                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (currentWeather.value == null)
                    _eventNetworkError.value = true
            }
        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CurrentWeatherViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}