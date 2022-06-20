package com.vsaytech.mvvmweather

import android.app.Application
import androidx.datastore.preferences.core.emptyPreferences
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Configuration.Provider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vsaytech.mvvmweather.util.LOCATION_KEY
import com.vsaytech.mvvmweather.util.LOCATION_PREFERENCE_KEY
import com.vsaytech.mvvmweather.util.locationDataStore
import com.vsaytech.mvvmweather.work.RefreshWeatherDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Override application to setup background work via WorkManager
 */
@HiltAndroidApp
class MyWeatherApplication : Application(), Provider {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    /**
     * Setup WorkManager background job to 'fetch' new network
     */
    private fun setupRecurringWork() {
        /*Use this if you want to schedule periodic work manager
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val inputLocationData = Data.Builder()
            .putString(LOCATION_KEY, "30.267153,-97.743057")
            .build()

        //MIN_PERIODIC_INTERVAL_MILLIS the min interval for work manager 15 minutes
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshWeatherDataWorker>(MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
            .setInputData(inputLocationData)
            .setConstraints(constraints)
            .build()

        Timber.d("Periodic Work request for sync is scheduled")
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshWeatherDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )*/

        //Read location from data store
        val dataStoreLocation = applicationContext.locationDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.d("Error reading preferences: ")
                    emit(emptyPreferences())
                } else {
                    Timber.d("Error reading preferences: ")
                    throw exception
                }
            }.map { preferences ->
                preferences[LOCATION_PREFERENCE_KEY] ?: ""
            }

        applicationScope.launch {
            if (dataStoreLocation.first().isEmpty()) {
                Timber.d("Datastore preferences[LOCATION_PREFERENCE_KEY] is null or empty")
            } else {
                val constraints = Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()

                val inputLocationData = Data.Builder()
                    .putString(LOCATION_KEY, dataStoreLocation.first())
                    .build()

                val repeatingRequest = OneTimeWorkRequestBuilder<RefreshWeatherDataWorker>()
                    .setInputData(inputLocationData)
                    .setConstraints(constraints)
                    .build()

                Timber.d("Periodic Work request for sync is scheduled")
                WorkManager.getInstance(applicationContext).enqueue(
                    repeatingRequest
                )
            }
        }
    }

    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}