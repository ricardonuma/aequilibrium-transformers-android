package com.aequilibrium.transformers

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var sApplication: MyApplication

        fun getInstance(): MyApplication {
            return sApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        sApplication = this
    }
}

