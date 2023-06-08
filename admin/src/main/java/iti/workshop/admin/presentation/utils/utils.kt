package iti.workshop.admin.presentation.utils

import android.app.AlertDialog
import android.content.Context

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