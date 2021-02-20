package com.crazie.android

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class ApplicationLoader : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}