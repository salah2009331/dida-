package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DentalTealLight,
    onPrimary = DentalTealDark,
    primaryContainer = DentalTealPrimary,
    onPrimaryContainer = Color(0xFF6FF6F4),
    secondary = DentalBlueLight,
    onSecondary = DentalBlueDark,
    background = Color(0xFF101919),
    surface = Color(0xFF142222),
    onSurface = Color(0xFFE1EAEA),
    surfaceVariant = Color(0xFF1E3232),
    onSurfaceVariant = Color(0xFFBCCBCB)
)

private val LightColorScheme = lightColorScheme(
    primary = DentalTealPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBCEEEE),
    onPrimaryContainer = Color(0xFF002020),
    secondary = DentalBlueSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC3EEF7),
    onSecondaryContainer = Color(0xFF001F26),
    background = DentalSurfaceLight,
    onBackground = Color(0xFF191C1C),
    surface = DentalCardBgLight,
    onSurface = Color(0xFF191C1C),
    surfaceVariant = Color(0xFFE6EFEF),
    onSurfaceVariant = Color(0xFF3F4949)
)

@Composable
fun DoctorDidaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our specialized dental branding by default
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
