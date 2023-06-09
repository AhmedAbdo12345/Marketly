package iti.mad.marketly.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.widget.EditText
import iti.mad.marketly.R

fun EditText.setCustomFocusChangeListener() {
    setOnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            view.setBackgroundResource(R.drawable.edit_text_border)
        } else {
            view.setBackgroundResource(R.drawable.edit_text_bck)
        }
    }
}

fun Context.hasNetwork(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}