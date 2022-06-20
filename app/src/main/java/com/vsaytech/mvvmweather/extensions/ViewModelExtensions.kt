package com.vsaytech.mvvmweather.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 * Transforms a [LiveData] into [MutableLiveData]
 *
 * @param T type
 * @return [MutableLiveData] emitting the same values
 */
fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
    val mediatorLiveData = MediatorLiveData<T>()
    mediatorLiveData.addSource(this) {
        mediatorLiveData.value = it
    }
    return mediatorLiveData
}