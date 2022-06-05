package com.vsaytech.mvvmweather.ui.currentweather

import android.location.Location

interface LastLocationListener {
    fun onLastLocationFound(location: Location)
    fun onCheckPermissonFailed()
}