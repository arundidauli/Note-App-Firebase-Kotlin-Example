package com.wingshield.technologies.noteappfirebasekotlinexample.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService

class Const {
    companion object{
        const val SUCCESS:String = "SUCCESS"
        const val ERROR:String = "ERROR"
        const val USER_NOTE:String = "userNote"
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}