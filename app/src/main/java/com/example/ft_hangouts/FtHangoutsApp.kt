package com.example.ft_hangouts

import android.app.Application

class FtHangoutsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeManager.initialize(this)
        LanguageManager.initialize(this)
    }
}
