package com.example.ft_hangouts.ui

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun applyLocale(context: Context, language: String): Context {
        val normalizedTag = language.replace('_', '-')
        val locale = Locale.forLanguageTag(normalizedTag).takeIf { it.language.isNotBlank() }
            ?: Locale.ENGLISH
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        return context.createConfigurationContext(config)
    }
}
