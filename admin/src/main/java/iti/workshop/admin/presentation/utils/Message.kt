package iti.workshop.admin.presentation.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import iti.workshop.admin.R

object Message {

    fun snakeMessage(
        context: Context,
        view: View,
        message: String?,
        isGood: Boolean,
        btnTitle: String?,
        clickListener: View.OnClickListener?,
    ): Snackbar? {
        val snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_LONG)
        if (isGood) {
            snackbar.setBackgroundTint(context.getColor(R.color.green))
        } else {
            snackbar.setBackgroundTint(context.getColor(R.color.red))
        }
        snackbar.setAction(btnTitle, clickListener)
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        return snackbar
    }


    fun snakeMessage(context: Context, view: View, message: String?, isGood: Boolean): Snackbar? {
        val snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_LONG)
        if (isGood) {
            snackbar.setBackgroundTint(context.getColor(R.color.green))
        } else {
            snackbar.setBackgroundTint(context.getColor(R.color.red))
        }
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        return snackbar
    }
}