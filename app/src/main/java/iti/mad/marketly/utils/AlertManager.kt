package iti.mad.marketly.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object AlertManager {
    fun nonFunctionalDialog(title:String,contex: Context,message:String,optinalMethod:()->Unit={}){
        val alert=AlertDialog.Builder(contex)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("OK"){dialogInterface, which ->
            optinalMethod()
        }
        val alertDialog: AlertDialog = alert.create()
        alertDialog.show()
    }
    fun functionalDialog(title:String,contex: Context,message:String,method:()->Unit):AlertDialog{
        val alert=AlertDialog.Builder(contex)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("OK"){dialogInterface, which ->
            method()
        }
        alert.setNegativeButton("Cancel"){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = alert.create()
        return alertDialog
    }
}