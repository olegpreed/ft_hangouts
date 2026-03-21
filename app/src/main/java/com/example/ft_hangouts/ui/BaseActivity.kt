package com.example.ft_hangouts.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import com.example.ft_hangouts.ui.theme.AppThemeVariant
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity : ComponentActivity() {
    protected val themeVariantState = mutableStateOf(AppThemeVariant.COLOR_1)
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
        updateThemeVariant()
    }

    protected fun updateThemeVariant() {
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedThemeVariant = prefs.getInt("theme_variant", AppThemeVariant.COLOR_1.id)
        themeVariantState.value = AppThemeVariant.fromId(savedThemeVariant)
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
        updateThemeVariant()
        
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("language", "en") ?: "en"
        if (savedLanguage != currentLanguage) {
            recreate()
        }
    }
}
