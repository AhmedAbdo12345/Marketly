package iti.workshop.admin.presentation.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context

fun Context.loadingDialog(title: String = "Loading Action",message:String="Please wait until finish .."): ProgressDialog = ProgressDialog(this).apply {
    setTitle(title)
    setMessage(message)
    create()
}
fun Context.alert(title:String,message:String,onAgree:()->Unit,onDisAgree:()->Unit){
    val builder = AlertDialog.Builder(this)

    builder.setTitle(title)
    builder.setMessage(message)

    builder.setPositiveButton("Yes") { _, _ ->
        onAgree()
    }

    builder.setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
        onDisAgree()
    }

    val dialog = builder.create()
    dialog.show()
}