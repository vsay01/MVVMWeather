package com.vsaytech.mvvmweather.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val LOCATION_KEY = "location"

val LOCATION_PREFERENCE_KEY = stringPreferencesKey(LOCATION_KEY)

val Context.locationDataStore: DataStore<Preferences> by preferencesDataStore(name = LOCATION_KEY)
