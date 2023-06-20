package iti.mad.marketly.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import iti.mad.marketly.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce

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




fun TextInputEditText.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int, after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            query.value = s.toString()
        }

        override fun afterTextChanged(s: Editable) {

        }
    })

    return query

}