package iti.mad.marketly.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import iti.mad.marketly.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


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
fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            // Handle search query submission
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrEmpty()&& newText.isNotBlank()) {
                query.value = newText.trimStart()
            }
            return false
        }
    })

    return query

}