package com.micahnyabuto.statussaver

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StatusSaverApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}