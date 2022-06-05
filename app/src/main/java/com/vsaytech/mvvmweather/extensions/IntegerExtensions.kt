package com.vsaytech.mvvmweather.extensions

import kotlin.Int.Companion.MIN_VALUE

fun Int?.ifNull(): Int {
    return this ?: MIN_VALUE
}