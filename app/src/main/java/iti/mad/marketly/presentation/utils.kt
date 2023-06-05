package iti.mad.marketly.presentation

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