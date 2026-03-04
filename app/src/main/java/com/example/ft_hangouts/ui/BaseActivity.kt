package com.example.ft_hangouts.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity : ComponentActivity() {
    protected val primaryColorState = mutableStateOf(Color(0xFF6650a4))
    private var currentLanguage: String? = null

    companion object {
        private var lastBackgroundTime: Long = 0
        private var runningActivities = 0
        private var showBackgroundToast = false
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "en") ?: "en"
        currentLanguage = language
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateThemeColor()
    }

    protected fun updateThemeColor() {
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedColorArgb = prefs.getInt("primary_color", Color(0xFF6650a4).toArgb())
        primaryColorState.value = Color(savedColorArgb)
    }

    override fun onStart() {
        super.onStart()
        if (runningActivities == 0 && showBackgroundToast) {
            if (lastBackgroundTime != 0L) {
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val timeStr = sdf.format(Date(lastBackgroundTime))
                Toast.makeText(this, timeStr, Toast.LENGTH_SHORT).show()
            }
            showBackgroundToast = false
        }
        runningActivities++
    }

    override fun onStop() {
        super.onStop()
        runningActivities--
        if (runningActivities == 0 && !isChangingConfigurations) {
            lastBackgroundTime = System.currentTimeMillis()
            showBackgroundToast = true
        }
    }

    override fun onResume() {
        super.onResume()
        updateThemeColor()
        
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("language", "en") ?: "en"
        if (savedLanguage != currentLanguage) {
            recreate()
        }
    }
}
