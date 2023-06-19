package iti.workshop.admin.data.shared

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import iti.workshop.admin.presentation.features.auth.model.User

class SharedManager private constructor(context: Context) {
    private var sharedPreferences: SharedPreferences
    private val SHARE_KEY = "shareRoom"

    init {
        sharedPreferences = context.getSharedPreferences(SHARE_KEY, Context.MODE_PRIVATE)
    }

    fun clearUser() :Boolean = sharedPreferences.edit().run{
        remove(USER_INFO)
        commit()
    }

    fun saveUser(user: User?):Boolean =  sharedPreferences.edit().run {
        putString(USER_INFO, Gson().toJson(user))
        commit()
    }

    val isUser: Boolean get() = sharedPreferences.contains(USER_INFO)

    fun getUser(): User = sharedPreferences.getString(USER_INFO, null).run{
        if (this != null)
            Gson().fromJson(this, User::class.java)
        else
            User()
    }

    val isFirstEntrance: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST, false)

    fun saveEntrance() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_FIRST, true)
        editor.apply()
    }

    companion object {
        const val IS_FIRST = "IS_FIRST"
        const val USER_INFO = "USER_INFO"

        @Volatile
        private lateinit var instance: SharedManager
        fun getInstance(context: Context): SharedManager {
            if (!::instance.isInitialized) instance = SharedManager(context)
            return instance
        }
    }
}