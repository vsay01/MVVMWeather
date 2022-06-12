package com.vsaytech.mvvmweather.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vsaytech.mvvmweather.data.database.getDatabase
import com.vsaytech.mvvmweather.data.repository.currentweather.CurrentWeatherRepository
import com.vsaytech.mvvmweather.util.LOCATION_KEY
import retrofit2.HttpException
import timber.log.Timber

class RefreshWeatherDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val location = inputData.getString(LOCATION_KEY)

        val repository = CurrentWeatherRepository(database)
        try {
            location?.let {
                repository.getCurrentWeather(location)
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