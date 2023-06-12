package iti.workshop.admin.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@SuppressLint("SimpleDateFormat")
fun handleDate(strDate:String?):String{
    strDate?.let {
        if (it.isBlank()){
            return it
        }

        val DATE_TIME_GLOBAL_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val DAY_PATTERN = "dd MMM"
        val format = SimpleDateFormat(DATE_TIME_GLOBAL_PATTERN, Locale.US)
        val date = format.parse(it)
        return SimpleDateFormat(DAY_PATTERN).format(date)
    }

    return "";

}

class TimeConverter {
    companion object {
        const val DATETIME_PATTERN = "dd MMM, hh:mm aa"
        const val TIME_PATTERN_HOUR = "hh aa"
        const val TIME_PATTERN = "hh:mm aa"
        const val DAY_PATTERN = "dd MMM"
        const val DATE_PATTERN = "dd-MMM-yyy"
        const val DATE_PATTERN_SLASH = "dd/MM/yyyy"
        const val DATE_TIME_GLOBAL_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
        @SuppressLint("SimpleDateFormat")
        fun convertTimestampToString(dt: Long, type: String): String? {
            val date = Date(dt)
            val format = SimpleDateFormat(type, Locale.ENGLISH)
            return format.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertStringToTimestamp(dt: String, type: String): Long {
            return SimpleDateFormat(type).parse(dt)?.time ?: 0
        }
    }
}