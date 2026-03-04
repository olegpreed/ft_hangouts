package com.example.ft_hangouts.ui

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun applyLocale(context: Context): Context {
        val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "en") ?: "en"
        
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        return context.createConfigurationContext(config)
    }
}
