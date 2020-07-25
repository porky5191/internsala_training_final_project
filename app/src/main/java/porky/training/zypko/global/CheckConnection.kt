package porky.training.zypko.global

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class CheckConnection(private val context: Context) {
    fun hasConnection(): Boolean {
        //Initialize connectivity manager
        val cm = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        //get active network information
        val activeNetwork : NetworkInfo ?= cm.activeNetworkInfo
        //check if active network is connected or not
        if (activeNetwork ?. isConnected != null){
            return activeNetwork.isConnected
        }else return false

    }

}