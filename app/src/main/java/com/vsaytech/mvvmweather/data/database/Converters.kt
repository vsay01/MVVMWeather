package com.vsaytech.mvvmweather.data.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vsaytech.mvvmweather.data.model.ConditionWS
import com.vsaytech.mvvmweather.data.model.ForecastDayWS
import java.lang.reflect.ParameterizedType


val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()
val typeForecastDayWSList: ParameterizedType = Types.newParameterizedType(MutableList::class.java, ForecastDayWS::class.java)
val jsonAdapter: JsonAdapter<List<ForecastDayWS>> = moshi.adapter(typeForecastDayWSList)
val jsonAdapterConditionWS: JsonAdapter<ConditionWS> = moshi.adapter(ConditionWS::class.java)

class Converters {
    @TypeConverter
    fun listForecastDayWSToJsonString(value: List<ForecastDayWS>?): String = jsonAdapter.toJson(value)

    @TypeConverter
    fun jsonForecastDayWSStringToList(value: String) = jsonAdapter.fromJson(value)

    @TypeConverter
    fun ConditionWSToJsonString(value: ConditionWS?): String = jsonAdapterConditionWS.toJson(value)

    @TypeConverter
    fun jsonStringToConditionWS(value: String) = jsonAdapterConditionWS.fromJson(value)
}