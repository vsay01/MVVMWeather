package com.vsaytech.mvvmweather.ui.uistate

sealed class NetworkResult<out T : Any> {
    class Loading<T : Any> : NetworkResult<T>()
    class Success<out T : Any>(val data: T) : NetworkResult<T>()
    class Error(val message: String, val cause: Exception? = null) : NetworkResult<Nothing>()
}