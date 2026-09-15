package com.example.ui.theme

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
    primary = RosePrimaryLight,
    onPrimary = Color(0xFF5C001E),
    primaryContainer = RosePrimaryDark,
    onPrimaryContainer = Color(0xFFFFD9DF),
    secondary = RoseSecondaryLight,
    onSecondary = Color(0xFF531122),
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = Color(0xFFFFD9DF),
    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF452B00),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = RosePrimary,
    onPrimary = Color.White,
    primaryContainer = RoseSecondaryContainer,
    onPrimaryContainer = RosePrimaryDark,
    secondary = RoseSecondary,
    onSecondary = Color.White,
    secondaryContainer = RoseSecondaryContainer,
    onSecondaryContainer = Color(0xFF880E4F),
    tertiary = RoseTertiary,
    onTertiary = Color.White,
    tertiaryContainer = RoseTertiaryContainer,
    onTertiaryContainer = Color(0xFF78350F),
    background = WarmBackground,
    onBackground = TextPrimary,
    surface = WarmSurface,
    onSurface = TextPrimary,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted romantic theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
