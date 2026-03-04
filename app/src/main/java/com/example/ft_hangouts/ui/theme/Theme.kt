package com.example.ft_hangouts.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun Ft_hangoutsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false to prioritize custom colors over wallpaper-based ones
    dynamicColor: Boolean = false,
    primary: Color? = null,
    secondary: Color? = null,
    tertiary: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> {
            DarkColorScheme.copy(
                primary = primary ?: DarkColorScheme.primary,
                secondary = secondary ?: DarkColorScheme.secondary,
                tertiary = tertiary ?: DarkColorScheme.tertiary
            )
        }
        else -> {
            LightColorScheme.copy(
                primary = primary ?: LightColorScheme.primary,
                secondary = secondary ?: LightColorScheme.secondary,
                tertiary = tertiary ?: LightColorScheme.tertiary
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
