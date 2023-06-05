package iti.workshop.admin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.SoftReference

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}