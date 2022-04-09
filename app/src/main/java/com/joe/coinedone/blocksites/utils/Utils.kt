package com.joe.coinedone.blocksites.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {

    fun getLocalTime(strTime: String): LocalTime {
        val localTime = LocalTime.parse(strTime, DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
        return localTime
    }

    fun checkTheTimes(strTimeFrom: String, strTimeTo: String): Boolean {
        try {
            val localFromTime = LocalTime.parse(strTimeFrom, DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
            val localToTime = LocalTime.parse(strTimeTo, DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
            return localToTime.isAfter(localFromTime)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }
}