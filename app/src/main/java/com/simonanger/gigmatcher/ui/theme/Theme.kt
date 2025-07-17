// ===== app/src/main/java/com/example/gigmatcher/ui/theme/Theme.kt =====
package com.simonanger.gigmatcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark Rock/Metal Color Palette
private val MetalBlack = Color(0xFF0D0D0D)
private val CharcoalGray = Color(0xFF1A1A1A)
private val DeepGray = Color(0xFF2A2A2A)
private val SteelGray = Color(0xFF3A3A3A)
private val CrimsonRed = Color(0xFFB71C1C)
private val BloodRed = Color(0xFF8B0000)
private val LightGray = Color(0xFFE0E0E0)
private val MetalSilver = Color(0xFFC0C0C0)
private val DarkRed = Color(0xFF660000)

private val DarkColorScheme = darkColorScheme(
    primary = CrimsonRed,
    onPrimary = Color.White,
    primaryContainer = DarkRed,
    onPrimaryContainer = LightGray,
    secondary = SteelGray,
    onSecondary = Color.White,
    secondaryContainer = DeepGray,
    onSecondaryContainer = LightGray,
    tertiary = MetalSilver,
    onTertiary = MetalBlack,
    tertiaryContainer = CharcoalGray,
    onTertiaryContainer = LightGray,
    error = BloodRed,
    onError = Color.White,
    errorContainer = DarkRed,
    onErrorContainer = LightGray,
    background = MetalBlack,
    onBackground = LightGray,
    surface = CharcoalGray,
    onSurface = LightGray,
    surfaceVariant = DeepGray,
    onSurfaceVariant = MetalSilver,
    outline = SteelGray,
    outlineVariant = DeepGray,
    inverseSurface = LightGray,
    inverseOnSurface = MetalBlack,
    inversePrimary = CrimsonRed,
    surfaceTint = CrimsonRed
)

private val LightColorScheme = lightColorScheme(
    primary = BloodRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = DarkRed,
    secondary = SteelGray,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = DeepGray,
    tertiary = DeepGray,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = MetalBlack,
    error = CrimsonRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = DarkRed,
    background = Color(0xFFF5F5F5),
    onBackground = MetalBlack,
    surface = Color.White,
    onSurface = MetalBlack,
    surfaceVariant = Color(0xFFF3F3F3),
    onSurfaceVariant = DeepGray,
    outline = SteelGray,
    outlineVariant = Color(0xFFCAC4D0),
    inverseSurface = DeepGray,
    inverseOnSurface = LightGray,
    inversePrimary = CrimsonRed,
    surfaceTint = BloodRed
)

@Composable
fun GigMatcherTheme(
    darkTheme: Boolean = true, // Default to dark theme for rock/metal aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}