package com.example.ft_hangouts.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft_hangouts.R
import com.example.ft_hangouts.ui.BaseActivity
import com.example.ft_hangouts.ui.theme.Ft_hangoutsTheme

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("language", "en") ?: "en"

        setContent {
            val primaryColor by primaryColorState
            var currentLanguage by remember { mutableStateOf(savedLanguage) }

            Ft_hangoutsTheme(primary = primaryColor) {
                SettingsScreen(
                    onBack = { finish() },
                    currentColor = primaryColor,
                    onColorSelect = { newColor ->
                        primaryColorState.value = newColor
                        prefs.edit().putInt("primary_color", newColor.toArgb()).apply()
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
    currentColor: Color,
    onColorSelect: (Color) -> Unit,
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.header_color),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                ColorOption(
                    color = Color(0xFF6650a4),
                    isSelected = currentColor == Color(0xFF6650a4),
                    onClick = { onColorSelect(Color(0xFF6650a4)) }
                )
                ColorOption(
                    color = Color(0xFF2196F3),
                    isSelected = currentColor == Color(0xFF2196F3),
                    onClick = { onColorSelect(Color(0xFF2196F3)) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { onLanguageSelect("en") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentLanguage == "en") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (currentLanguage == "en") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.english))
                }
                Button(
                    onClick = { onLanguageSelect("ru") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentLanguage == "ru") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (currentLanguage == "ru") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.russian))
                }
            }
        }
    }
}

@Composable
fun ColorOption(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.background(color.copy(alpha = 0.5f)) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
            )
        }
    }
}
