package com.wingshield.technologies.noteappfirebasekotlinexample

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MySingleton:Application() {

    private lateinit var mInstance:MySingleton

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        mInstance = this
    }

    @Synchronized
    fun getInstance(): MySingleton? {
        return mInstance
    }

}