package com.example.ft_hangouts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class AppThemeVariant(val id: Int) {
    COLOR_1(0),
    COLOR_2(1);

    companion object {
        fun fromId(id: Int): AppThemeVariant {
            return entries.firstOrNull { it.id == id } ?: COLOR_1
        }
    }
}

private val Color1LightScheme = lightColorScheme(
    primary = primaryLight1,
    onPrimary = onPrimaryLight1,
    primaryContainer = primaryContainerLight1,
    onPrimaryContainer = onPrimaryContainerLight1,
    secondary = secondaryLight1,
    onSecondary = onSecondaryLight1,
    secondaryContainer = secondaryContainerLight1,
    onSecondaryContainer = onSecondaryContainerLight1,
    tertiary = tertiaryLight1,
    onTertiary = onTertiaryLight1,
    tertiaryContainer = tertiaryContainerLight1,
    onTertiaryContainer = onTertiaryContainerLight1,
    error = errorLight1,
    onError = onErrorLight1,
    errorContainer = errorContainerLight1,
    onErrorContainer = onErrorContainerLight1,
    background = backgroundLight1,
    onBackground = onBackgroundLight1,
    surface = surfaceLight1,
    onSurface = onSurfaceLight1,
    surfaceVariant = surfaceVariantLight1,
    onSurfaceVariant = onSurfaceVariantLight1,
    outline = outlineLight1,
    outlineVariant = outlineVariantLight1,
    scrim = scrimLight1,
    inverseSurface = inverseSurfaceLight1,
    inverseOnSurface = inverseOnSurfaceLight1,
    inversePrimary = inversePrimaryLight1,
    surfaceDim = surfaceDimLight1,
    surfaceBright = surfaceBrightLight1,
    surfaceContainerLowest = surfaceContainerLowestLight1,
    surfaceContainerLow = surfaceContainerLowLight1,
    surfaceContainer = surfaceContainerLight1,
    surfaceContainerHigh = surfaceContainerHighLight1,
    surfaceContainerHighest = surfaceContainerHighestLight1,
)

private val Color1DarkScheme = darkColorScheme(
    primary = primaryDark1,
    onPrimary = onPrimaryDark1,
    primaryContainer = primaryContainerDark1,
    onPrimaryContainer = onPrimaryContainerDark1,
    secondary = secondaryDark1,
    onSecondary = onSecondaryDark1,
    secondaryContainer = secondaryContainerDark1,
    onSecondaryContainer = onSecondaryContainerDark1,
    tertiary = tertiaryDark1,
    onTertiary = onTertiaryDark1,
    tertiaryContainer = tertiaryContainerDark1,
    onTertiaryContainer = onTertiaryContainerDark1,
    error = errorDark1,
    onError = onErrorDark1,
    errorContainer = errorContainerDark1,
    onErrorContainer = onErrorContainerDark1,
    background = backgroundDark1,
    onBackground = onBackgroundDark1,
    surface = surfaceDark1,
    onSurface = onSurfaceDark1,
    surfaceVariant = surfaceVariantDark1,
    onSurfaceVariant = onSurfaceVariantDark1,
    outline = outlineDark1,
    outlineVariant = outlineVariantDark1,
    scrim = scrimDark1,
    inverseSurface = inverseSurfaceDark1,
    inverseOnSurface = inverseOnSurfaceDark1,
    inversePrimary = inversePrimaryDark1,
    surfaceDim = surfaceDimDark1,
    surfaceBright = surfaceBrightDark1,
    surfaceContainerLowest = surfaceContainerLowestDark1,
    surfaceContainerLow = surfaceContainerLowDark1,
    surfaceContainer = surfaceContainerDark1,
    surfaceContainerHigh = surfaceContainerHighDark1,
    surfaceContainerHighest = surfaceContainerHighestDark1,
)

private val Color2LightScheme = lightColorScheme(
    primary = primaryLight2,
    onPrimary = onPrimaryLight2,
    primaryContainer = primaryContainerLight2,
    onPrimaryContainer = onPrimaryContainerLight2,
    secondary = secondaryLight2,
    onSecondary = onSecondaryLight2,
    secondaryContainer = secondaryContainerLight2,
    onSecondaryContainer = onSecondaryContainerLight2,
    tertiary = tertiaryLight2,
    onTertiary = onTertiaryLight2,
    tertiaryContainer = tertiaryContainerLight2,
    onTertiaryContainer = onTertiaryContainerLight2,
    error = errorLight2,
    onError = onErrorLight2,
    errorContainer = errorContainerLight2,
    onErrorContainer = onErrorContainerLight2,
    background = backgroundLight2,
    onBackground = onBackgroundLight2,
    surface = surfaceLight2,
    onSurface = onSurfaceLight2,
    surfaceVariant = surfaceVariantLight2,
    onSurfaceVariant = onSurfaceVariantLight2,
    outline = outlineLight2,
    outlineVariant = outlineVariantLight2,
    scrim = scrimLight2,
    inverseSurface = inverseSurfaceLight2,
    inverseOnSurface = inverseOnSurfaceLight2,
    inversePrimary = inversePrimaryLight2,
    surfaceDim = surfaceDimLight2,
    surfaceBright = surfaceBrightLight2,
    surfaceContainerLowest = surfaceContainerLowestLight2,
    surfaceContainerLow = surfaceContainerLowLight2,
    surfaceContainer = surfaceContainerLight2,
    surfaceContainerHigh = surfaceContainerHighLight2,
    surfaceContainerHighest = surfaceContainerHighestLight2,
)

private val Color2DarkScheme = darkColorScheme(
    primary = primaryDark2,
    onPrimary = onPrimaryDark2,
    primaryContainer = primaryContainerDark2,
    onPrimaryContainer = onPrimaryContainerDark2,
    secondary = secondaryDark2,
    onSecondary = onSecondaryDark2,
    secondaryContainer = secondaryContainerDark2,
    onSecondaryContainer = onSecondaryContainerDark2,
    tertiary = tertiaryDark2,
    onTertiary = onTertiaryDark2,
    tertiaryContainer = tertiaryContainerDark2,
    onTertiaryContainer = onTertiaryContainerDark2,
    error = errorDark2,
    onError = onErrorDark2,
    errorContainer = errorContainerDark2,
    onErrorContainer = onErrorContainerDark2,
    background = backgroundDark2,
    onBackground = onBackgroundDark2,
    surface = surfaceDark2,
    onSurface = onSurfaceDark2,
    surfaceVariant = surfaceVariantDark2,
    onSurfaceVariant = onSurfaceVariantDark2,
    outline = outlineDark2,
    outlineVariant = outlineVariantDark2,
    scrim = scrimDark2,
    inverseSurface = inverseSurfaceDark2,
    inverseOnSurface = inverseOnSurfaceDark2,
    inversePrimary = inversePrimaryDark2,
    surfaceDim = surfaceDimDark2,
    surfaceBright = surfaceBrightDark2,
    surfaceContainerLowest = surfaceContainerLowestDark2,
    surfaceContainerLow = surfaceContainerLowDark2,
    surfaceContainer = surfaceContainerDark2,
    surfaceContainerHigh = surfaceContainerHighDark2,
    surfaceContainerHighest = surfaceContainerHighestDark2,
)

@Composable
fun Ft_hangoutsTheme(
    themeVariant: AppThemeVariant = AppThemeVariant.COLOR_1,
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    val colorScheme = when (themeVariant) {
        AppThemeVariant.COLOR_1 -> if (isDarkTheme) Color1DarkScheme else Color1LightScheme
        AppThemeVariant.COLOR_2 -> if (isDarkTheme) Color2DarkScheme else Color2LightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
