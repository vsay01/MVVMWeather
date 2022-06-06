package com.vsaytech.mvvmweather.data.model

import com.vsaytech.mvvmweather.extensions.ifNull

data class ConditionXWS(
    val code: Int?,
    val icon: String?,
    val text: String?
) {
    fun getConditionIcon() = "https://" + icon.ifNull().removePrefix("//")
}