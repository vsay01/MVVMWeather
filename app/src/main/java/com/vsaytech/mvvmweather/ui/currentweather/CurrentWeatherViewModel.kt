package com.vsaytech.mvvmweather.ui.currentweather

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vsaytech.mvvmweather.data.database.getDatabase
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDailyForecastDomainModel
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDomainModel
import com.vsaytech.mvvmweather.data.repository.currentweather.CurrentWeatherRepository
import com.vsaytech.mvvmweather.ui.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.domain.CurrentWeatherDailyForecast
import com.vsaytech.mvvmweather.util.LOCATION_PREFERENCE_KEY
import com.vsaytech.mvvmweather.util.locationDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class CurrentWeatherViewModel(private val app: Application) : AndroidViewModel(app) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val currentWeatherRepository = CurrentWeatherRepository(getDatabase(app))

    /**
     * A currentWeather displayed on the screen.
     */
    val currentWeather: LiveData<CurrentWeather> = Transformations.map(
        currentWeatherRepository.currentWeather
            .catch {
                _eventNetworkError.value = true
            }.asLiveData()
    ) {
        it.asCurrentWeatherDomainModel()
    }

    /**
     * A dailyForecastWeather displayed on the screen.
     */
    val dailyForecastWeatherList: LiveData<List<CurrentWeatherDailyForecast>> = Transformations.map(
        currentWeatherRepository.currentWeather
            .catch {
                _eventNetworkError.value = true
            }.asLiveData()
    ) {
        it.forecast.asCurrentWeatherDailyForecastDomainModel()
    }

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

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun refreshCurrentWeatherFromRepository(location: String) {
        viewModelScope.launch {
            try {
                saveLocationDataStore(location)
                currentWeatherRepository.getCurrentWeather(location)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (currentWeather.value == null)
                    _eventNetworkError.value = true
            }
        }
    }

    private suspend fun saveLocationDataStore(locationParams: String) {
        app.locationDataStore.edit { location ->
            location[LOCATION_PREFERENCE_KEY] = locationParams
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