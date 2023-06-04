package iti.mad.marketly.data.source.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import iti.mad.marketly.data.Constants

object SharedPreferenceManager {
    lateinit var shared:SharedPreferences
    fun initPreferences(context:Context){
        shared=PreferenceManager.getDefaultSharedPreferences(context)
    }
    fun checkPreferences(context: Context){
        if (shared==null){
            initPreferences(context)
        }
    }
    fun saveCurrency(cur:String,context: Context){
        checkPreferences(context)
        shared.edit().putString(Constants.CURRENCY,cur)
    }
    fun getSavedCurrency(context: Context):String?{
        return shared.getString(Constants.CURRENCY,"")
    }
    fun saveDefaultAddress(address:String,context: Context){
        checkPreferences(context)
        shared.edit().putString(Constants.DEFAULTADDRESS,address)
    }
    fun getDefaultAddress(context: Context):String?{
        return shared.getString(Constants.DEFAULTADDRESS,"")
    }
}