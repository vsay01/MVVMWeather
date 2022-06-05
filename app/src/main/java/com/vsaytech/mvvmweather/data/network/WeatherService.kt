package com.vsaytech.mvvmweather.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vsaytech.mvvmweather.BuildConfig
import com.vsaytech.mvvmweather.data.model.CurrentWeatherWS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("forecast.json")
    suspend fun getCurrentWeatherByLocation(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 10
    ): CurrentWeatherWS
}

object WeatherNetwork {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()

    init {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }

        retrofit.baseUrl(BuildConfig.WEATHER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
    }

    val currentWeather: WeatherService = retrofit.build().create(WeatherService::class.java)
}