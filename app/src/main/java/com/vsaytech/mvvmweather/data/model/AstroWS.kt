package com.vsaytech.mvvmweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AstroWS(
    val moon_illumination: String?,
    val moon_phase: String?,
    val moonrise: String?,
    val moonset: String?,
    val sunrise: String?,
    val sunset: String?
): Parcelable