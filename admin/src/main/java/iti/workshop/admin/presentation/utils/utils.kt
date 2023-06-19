package iti.workshop.admin.presentation.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager


fun Context.hasNetwork(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun Context.loadingDialog(
    title: String = "Loading Action",
    message: String = "Please wait until finish .."
): ProgressDialog = ProgressDialog(this).apply {
    setTitle(title)
    setMessage(message)
    create()
}

fun Context.alertDialog(
    title: String,
    message: String,
    onAgree: () -> Unit,
    onDisAgree: () -> Unit
) = AlertDialog.Builder(this).run {
    setTitle(title)
    setMessage(message)

    setPositiveButton("Yes") { _, _ ->
        onAgree()
    }

    setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
        onDisAgree()
    }
    create()
    show()
}