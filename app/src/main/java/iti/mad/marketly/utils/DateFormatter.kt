package iti.mad.marketly.utils

import android.icu.text.SimpleDateFormat
import java.util.Calendar

object DateFormatter {
    fun getCurrentDate():String{
        val format = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(format)
        val date = Calendar.getInstance().time
        return formatter.format(date)
    }
}