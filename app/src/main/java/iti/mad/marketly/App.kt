package iti.mad.marketly

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        /*StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog()
            .build())
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
               // .detectLeakedClosableObjects()
                .penaltyLog()
               // .penaltyDeath()
                .build())*/
        //)


        AppDependencies.initialization()
    }

}