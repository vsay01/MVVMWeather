package com.vsaytech.mvvmweather.extensions

fun String?.ifNull(): String {
    return this ?: ""
}