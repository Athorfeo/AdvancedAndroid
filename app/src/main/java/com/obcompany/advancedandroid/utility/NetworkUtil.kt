package com.obcompany.advancedandroid.utility

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {
    companion object{
        @JvmStatic
        fun isOnline(context: Context): Boolean{
            val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null
        }
    }
}