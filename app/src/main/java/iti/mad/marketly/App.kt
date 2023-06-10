package iti.mad.marketly

import android.app.Application

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        AppDependencies.initialization(applicationContext)
    }
}