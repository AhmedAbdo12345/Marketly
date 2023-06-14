package iti.mad.marketly.data.source.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import iti.mad.marketly.utils.Constants

object SharedPreferenceManager {
    lateinit var shared: SharedPreferences

    fun initPreferences(context: Context) {
        shared = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun checkPreferences(context: Context) {
        if (!::shared.isInitialized) {
            initPreferences(context)
        }
    }

    fun saveCurrency(cur: String, context: Context) {
        checkPreferences(context)
        shared.edit().putString(Constants.CURRENCY, cur).apply()
    }

    fun getSavedCurrency(context: Context): String? {
        checkPreferences(context)
        return shared.getString(Constants.CURRENCY, "")
    }

    fun saveDefaultAddress(address: String, context: Context) {
        checkPreferences(context)
        shared.edit().putString(Constants.DEFAULTADDRESS, address).apply()
    }

    fun getDefaultAddress(context: Context): String? {
        checkPreferences(context)
        return shared.getString(Constants.DEFAULTADDRESS, "")
    }

    fun saveDefaultExchangeRate(exchangeRate: Double, context: Context) {
        checkPreferences(context)
        shared.edit().putFloat(Constants.EXCHANGERATE, exchangeRate.toFloat()).apply()
    }

    fun getDefaultExchangeRate(context: Context): Double? {
        checkPreferences(context)
        return shared.getFloat(Constants.EXCHANGERATE, 0.0F).toDouble()
    }

    fun saveUserData(context: Context, userId: String, email: String, userName: String) {
        checkPreferences(context)
        shared.edit().putBoolean(Constants.IS_LOGIN, true).apply()
        shared.edit().putString(Constants.USER_EMAIL, email).apply()
        shared.edit().putString(Constants.USER_ID, userId).apply()

        shared.edit().putString(Constants.USER_NAME, userName).apply()
    }

    fun saveFirebaseUID(firebaseUID: String) {
        shared.edit().putString(Constants.FIREBASE_USER_ID, firebaseUID).apply()
    }

    fun retreiveUserData(context: Context) {
        checkPreferences(context)
        shared.getBoolean(Constants.IS_LOGIN, false)
        shared.getString(Constants.USER_EMAIL, "")
        shared.getString(Constants.USER_ID, "")
        shared.getString(Constants.USER_NAME, "")
    }

    fun getFirebaseUID(context: Context): String? {
        checkPreferences(context)
        return shared.getString(Constants.FIREBASE_USER_ID, "")
    }

    fun isUserLogin(context: Context): Boolean {
        checkPreferences(context)
        return shared.getBoolean(Constants.IS_LOGIN, false)
    }
}