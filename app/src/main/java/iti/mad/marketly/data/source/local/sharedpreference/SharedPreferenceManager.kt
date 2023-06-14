package iti.mad.marketly.data.source.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import iti.mad.marketly.utils.Constants

object SharedPreferenceManager {
  private lateinit var shared:SharedPreferences
    fun initPreferences(context:Context){
        shared=PreferenceManager.getDefaultSharedPreferences(context)
    }
    fun checkPreferences(context: Context){
        if (!this::shared.isInitialized){
            initPreferences(context)
        }
    }
    fun saveCurrency(cur:String,context: Context){
        checkPreferences(context)
        shared.edit().putString(Constants.CURRENCY,cur).apply()
    }
    fun getSavedCurrency(context: Context):String?{
        checkPreferences(context)
        return shared.getString(Constants.CURRENCY,"")
    }
    fun saveDefaultAddress(address:String,context: Context){
        checkPreferences(context)
        shared.edit().putString(Constants.DEFAULTADDRESS,address).apply()
    }
    fun getDefaultAddress(context: Context):String?{
        checkPreferences(context)
        return shared.getString(Constants.DEFAULTADDRESS,"")
    }
    fun saveDefaultExchangeRate(exchangeRate:Double,context: Context){
        checkPreferences(context)
        shared.edit().putFloat(Constants.EXCHANGERATE,exchangeRate.toFloat()).apply()
    }
    fun getDefaultExchangeRate(context: Context):Double?{
        checkPreferences(context)
        return shared.getFloat(Constants.EXCHANGERATE, 0.0F).toDouble()
    }
    fun saveUserName(userName:String,context: Context){
        checkPreferences(context)
        shared.edit().putString(Constants.USER_NAME,userName).apply()
    }
    fun getUserName(context: Context):String?{
        checkPreferences(context)
        return shared.getString(Constants.USER_NAME,"")
    }
}