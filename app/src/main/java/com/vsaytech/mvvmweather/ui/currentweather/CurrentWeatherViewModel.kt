package com.vsaytech.mvvmweather.ui.currentweather

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDailyForecastDomainModel
import com.vsaytech.mvvmweather.data.network.asCurrentWeatherDomainModel
import com.vsaytech.mvvmweather.data.repository.currentweather.CurrentWeatherRepository
import com.vsaytech.mvvmweather.ui.domain.CurrentWeather
import com.vsaytech.mvvmweather.ui.domain.CurrentWeatherDailyForecast
import com.vsaytech.mvvmweather.ui.uistate.NetworkResult
import com.vsaytech.mvvmweather.util.LOCATION_PREFERENCE_KEY
import com.vsaytech.mvvmweather.util.locationDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val app: Application,
    private val currentWeatherRepository: CurrentWeatherRepository
) : AndroidViewModel(app) {

    /**
     * A currentWeather displayed on the screen.
     */
    // publicly exposed live data, not mutable
    val currentWeatherStateFlow: StateFlow<NetworkResult<CurrentWeather>>
        get() = currentWeatherMutableStateFlow

    private val currentWeatherMutableStateFlow: MutableStateFlow<NetworkResult<CurrentWeather>> = MutableStateFlow(NetworkResult.Loading())

    /**
     * A dailyForecastWeather displayed on the screen.
     */
    // publicly exposed live data, not mutable
    val dailyForecastWeatherListStateFlow: StateFlow<NetworkResult<List<CurrentWeatherDailyForecast>>>
        get() = dailyForecastWeatherListMutableStateFlow

    private val dailyForecastWeatherListMutableStateFlow: MutableStateFlow<NetworkResult<List<CurrentWeatherDailyForecast>>> =
        MutableStateFlow(NetworkResult.Loading())

    init {
        viewModelScope.launch {
            currentWeatherRepository.currentWeather
                // Update View with the latest currentWeatherDB
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all of its collectors
                .collect { currentWeather ->
                    currentWeatherMutableStateFlow.value = NetworkResult.Success(currentWeather.asCurrentWeatherDomainModel())

                    dailyForecastWeatherListMutableStateFlow.value =
                        NetworkResult.Success(currentWeather.forecast.asCurrentWeatherDailyForecastDomainModel())
                }
        }
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun refreshCurrentWeatherFromRepository(location: String) {
        viewModelScope.launch {
            try {
                currentWeatherMutableStateFlow.value = NetworkResult.Loading()
                saveLocationDataStore(location)
                currentWeatherRepository.getCurrentWeather(location)
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                currentWeatherMutableStateFlow.value = NetworkResult.Error("Refresh current weather from repository", networkError)
            }
        }
    }

    private suspend fun saveLocationDataStore(locationParams: String) {
        app.locationDataStore.edit { location ->
            location[LOCATION_PREFERENCE_KEY] = locationParams
        }
    }
}