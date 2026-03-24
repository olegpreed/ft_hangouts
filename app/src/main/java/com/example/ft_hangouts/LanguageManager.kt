package com.example.ft_hangouts

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

object LanguageManager {
    private lateinit var prefs: SharedPreferences
    val currentLanguageState = mutableStateOf("en")
    private var changeListener: ((String) -> Unit)? = null
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        // Read language only once at app startup
        val savedLanguage = prefs.getString("language", "en") ?: "en"
        currentLanguageState.value = savedLanguage
        
        // Listen for language changes from other activities
        prefs.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == "language") {
                val newLanguage = prefs.getString("language", "en") ?: "en"
                if (newLanguage != currentLanguageState.value) {
                    currentLanguageState.value = newLanguage
                    changeListener?.invoke(newLanguage)
                }
            }
        }
    }
    
    fun setLanguage(language: String) {
        currentLanguageState.value = language
        prefs.edit().putString("language", language).apply()
    }
    
    fun onLanguageChanged(listener: (String) -> Unit) {
        changeListener = listener
    }
}
