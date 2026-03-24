package com.example.ft_hangouts

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.example.ft_hangouts.ui.theme.AppThemeVariant

object ThemeManager {
    private lateinit var prefs: SharedPreferences
    val themeVariantState = mutableStateOf(AppThemeVariant.COLOR_1)
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        // Read theme only once at app startup
        val savedThemeVariant = prefs.getInt("theme_variant", AppThemeVariant.COLOR_1.id)
        themeVariantState.value = AppThemeVariant.fromId(savedThemeVariant)
        
        // Listen for theme changes from other activities
        prefs.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == "theme_variant") {
                val newTheme = prefs.getInt("theme_variant", AppThemeVariant.COLOR_1.id)
                themeVariantState.value = AppThemeVariant.fromId(newTheme)
            }
        }
    }
    
    fun setTheme(variant: AppThemeVariant) {
        themeVariantState.value = variant
        prefs.edit().putInt("theme_variant", variant.id).apply()
    }
}
