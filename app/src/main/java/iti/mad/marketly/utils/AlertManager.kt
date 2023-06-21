package iti.mad.marketly.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import iti.mad.marketly.databinding.AdsAlertDialogBinding

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
    fun functionalDialog(title:String,contex: Context,message:String,method:()->Unit,cancleMethod:()->Unit = {}):AlertDialog{
        val alert=AlertDialog.Builder(contex)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("OK"){dialogInterface, which ->
            method()
        }
        alert.setNegativeButton("Cancel"){dialogInterface, which ->
            cancleMethod()
        }
        val alertDialog: AlertDialog = alert.create()
        return alertDialog
    }
    @SuppressLint("ServiceCast")
    fun customDialog(title:String, contex: Context, message:String, method:()->Unit):AlertDialog{
        val dialog = AlertDialog.Builder(contex)
        val inflater =contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = AdsAlertDialogBinding.inflate(inflater)

        dialog.setView(binding.root)
        val alert = dialog.create()
        binding.ctv1.text =title
        binding.ctv2.text =message
        binding.back.setOnClickListener(View.OnClickListener {
            method()
            AlertManager.dismessDialog(alert)
        })

        return  alert
    }
    fun dismessDialog(alertDialog: AlertDialog){
        alertDialog.dismiss()
    }
}