package iti.workshop.admin.presentation.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


fun Context.hasNetwork(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}


fun Activity.hideSoftKey(){
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.chooseImage() {
    val optionsMenu = arrayOf<CharSequence>(
        "Take Photo",
        "Choose from Gallery",
    )

    val builder = AlertDialog.Builder(this)
    builder.setItems(optionsMenu) { dialogInterface, i ->
        if (optionsMenu[i] == "Take Photo") {

            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        } else if (optionsMenu[i] == "Choose from Gallery") {
            // choose from  external storage
            val pickPhoto =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 1)
        }
    }
    builder.show()
}
fun persistImage(activity: Activity, bitmap: Bitmap?, name: String): File {
    val filesDir = activity.applicationContext.filesDir
    val imageFile = File(filesDir, "$name.jpg")
    val os: OutputStream
    try {
        os = FileOutputStream(imageFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
    } catch (e: Exception) {
        Log.e("Error", "Error writing bitmap", e)
    }
    return imageFile
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