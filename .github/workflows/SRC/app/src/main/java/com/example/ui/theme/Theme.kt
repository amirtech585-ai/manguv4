package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.ThemeMode

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkManguPrimary,
    onPrimary = Color(0xFF003822),
    primaryContainer = ManguGreenDark,
    onPrimaryContainer = Color(0xFF7DF5BE),
    secondary = ManguOrangeAccent,
    onSecondary = Color(0xFF452200),
    secondaryContainer = Color(0xFF663600),
    background = DarkManguBackground,
    surface = DarkManguSurface,
    onBackground = Color(0xFFE2EBE5),
    onSurface = Color(0xFFE2EBE5),
    surfaceVariant = Color(0xFF223028),
    onSurfaceVariant = Color(0xFFA6B7AE),
    outline = Color(0xFF3B4D43)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ManguGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ManguGreenContainer,
    onPrimaryContainer = OnManguGreenContainer,
    secondary = ManguOrangeAccent,
    onSecondary = Color.White,
    secondaryContainer = ManguOrangeContainer,
    onSecondaryContainer = ManguOrangeDark,
    background = ManguBackground,
    surface = ManguSurface,
    onBackground = ManguTextPrimary,
    onSurface = ManguTextPrimary,
    surfaceVariant = Color(0xFFEFF4F1),
    onSurfaceVariant = ManguTextSecondary,
    outline = ManguCardBorder
  )

@Composable
fun MyApplicationTheme(
  themeMode: ThemeMode? = null,
  darkTheme: Boolean = if (themeMode != null) {
    when (themeMode) {
      ThemeMode.LIGHT -> false
      ThemeMode.DARK -> true
      ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
  } else true,
  // Set to false to preserve our custom Masaka brand aesthetic consistently
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val isDark = if (themeMode != null) {
    when (themeMode) {
      ThemeMode.LIGHT -> false
      ThemeMode.DARK -> true
      ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
  } else {
    darkTheme
  }
  val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun ManguDeliveryTheme(
  themeMode: ThemeMode? = null,
  darkTheme: Boolean = if (themeMode != null) {
    when (themeMode) {
      ThemeMode.LIGHT -> false
      ThemeMode.DARK -> true
      ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
  } else true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MyApplicationTheme(themeMode = themeMode, darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}


