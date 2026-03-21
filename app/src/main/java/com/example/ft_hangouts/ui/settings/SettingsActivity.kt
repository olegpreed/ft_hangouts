package com.example.ft_hangouts.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft_hangouts.R
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.landscapeLeftSafeArea
import com.example.ft_hangouts.ui.theme.AppThemeVariant
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("language", "en") ?: "en"

        setContent {
            val themeVariant by themeVariantState
            var currentLanguage by remember { mutableStateOf(savedLanguage) }

            Ft_hangoutsTheme(themeVariant = themeVariant) {
                SettingsScreen(
                    onBack = { finish() },
                    currentThemeVariant = themeVariant,
                    onThemeSelect = { selectedVariant ->
                        themeVariantState.value = selectedVariant
                        prefs.edit().putInt("theme_variant", selectedVariant.id).apply()
                    },
                    currentLanguage = currentLanguage,
                    onLanguageSelect = { lang ->
                        if (currentLanguage != lang) {
                            currentLanguage = lang
                            prefs.edit().putString("language", lang).apply()
                            // Recreate is handled by BaseActivity onResume or manually here
                            recreate()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    currentThemeVariant: AppThemeVariant,
    onThemeSelect: (AppThemeVariant) -> Unit,
    currentLanguage: String,
    onLanguageSelect: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        modifier = Modifier.landscapeLeftSafeArea()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.theme),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                SegmentedButton(
                    selected = currentThemeVariant == AppThemeVariant.COLOR_1,
                    onClick = { onThemeSelect(AppThemeVariant.COLOR_1) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    label = { Text(stringResource(R.string.theme_color1)) }
                )
                SegmentedButton(
                    selected = currentThemeVariant == AppThemeVariant.COLOR_2,
                    onClick = { onThemeSelect(AppThemeVariant.COLOR_2) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    label = { Text(stringResource(R.string.theme_color2)) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = currentLanguage == "en",
                    onClick = { onLanguageSelect("en") },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    label = { Text(stringResource(R.string.english)) }
                )
                SegmentedButton(
                    selected = currentLanguage == "ru",
                    onClick = { onLanguageSelect("ru") },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    label = { Text(stringResource(R.string.russian)) }
                )
            }
        }
    }
}
