package iti.workshop.admin.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

class InternetConnection(context: Context) : LiveData<Pair<String?,Boolean?>>() {
    private val connectivityManager: ConnectivityManager

    private var firstTimeFlag:Boolean = true
    init {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback: NetworkCallback
        get() = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (!firstTimeFlag)
                    postValue(Pair("Network Connection come back",true))
                else
                    firstTimeFlag = false
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                if (firstTimeFlag) firstTimeFlag = false
                postValue(Pair("There is no Internet Connection",false))
            }
        }

    override fun onInactive() {
        super.onInactive()
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActive() {
        super.onActive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    private val networkRequest: NetworkRequest
        get() = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
}