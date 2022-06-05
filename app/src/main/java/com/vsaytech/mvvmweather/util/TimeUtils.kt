package com.vsaytech.mvvmweather.util

import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"
const val DATE_TIME_FORMAT_SECONDARY = "yyyy-MM-dd"
const val DAY_FORMAT = "EEEE"
const val MONTH_DAY_FORMAT = "MMM/dd"
const val TIME_FORMAT = "HH:mm"
const val TODAY = "Today"

fun getDayNameFromDateTimeString(dateTime: String): String? {
    val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    val date = format.parse(dateTime)
    val day = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())
    return date?.let {
        if (isDateCurrentDay(day.format(it))) return TODAY else return day.format(it)
    }
}

fun getDayNameFromDateString(dateTime: String): String? {
    val format = SimpleDateFormat(DATE_TIME_FORMAT_SECONDARY, Locale.getDefault())
    val date = format.parse(dateTime)
    val day = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())
    return date?.let {
        if (isDateCurrentDay(day.format(it))) return TODAY else return day.format(it)
    }
}

fun getMonthDayFromDateTimeString(dateTime: String): String? {
    val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    val date = format.parse(dateTime)
    val mothDay = SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault())
    return date?.let { mothDay.format(it) }
}

fun getMonthDayFromDateString(dateTime: String): String? {
    val format = SimpleDateFormat(DATE_TIME_FORMAT_SECONDARY, Locale.getDefault())
    val date = format.parse(dateTime)
    val mothDay = SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault())
    return date?.let { mothDay.format(it) }
}

fun getTimeFromDateString(dateTime: String): String? {
    val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    val date = format.parse(dateTime)
    val time = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    return date?.let { time.format(it) }
}

private fun isDateCurrentDay(day: String): Boolean {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())
    val dayOfWeek = sdf.format(calendar.time)
    if (dayOfWeek.contentEquals(day, true)) {
        return true
    }
    return false
}