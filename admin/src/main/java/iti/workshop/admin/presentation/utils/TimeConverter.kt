package iti.workshop.admin.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


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