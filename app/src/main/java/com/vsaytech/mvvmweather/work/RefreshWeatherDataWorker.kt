package com.vsaytech.mvvmweather.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vsaytech.mvvmweather.data.repository.currentweather.CurrentWeatherRepository
import com.vsaytech.mvvmweather.util.LOCATION_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import timber.log.Timber

@HiltWorker
class RefreshWeatherDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val currentWeatherRepository: CurrentWeatherRepository
) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val location = inputData.getString(LOCATION_KEY)

        try {
            location?.let {
                currentWeatherRepository.getCurrentWeather(location)
            } ?: kotlin.run {
                Timber.d("Location is null or empty")
            }
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "com.vsaytech.mvvmweather.work.RefreshWeatherDataWorker"
    }
}