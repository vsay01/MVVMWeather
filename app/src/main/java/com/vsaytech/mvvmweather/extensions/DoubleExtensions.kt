package com.vsaytech.mvvmweather.extensions

import kotlin.Double.Companion.MIN_VALUE

fun Double?.ifNull(): Double {
    return this ?: MIN_VALUE
}