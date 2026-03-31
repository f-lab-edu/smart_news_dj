package com.djyoo.smartnews.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
    lightColorScheme(
        primary = Primary,
        secondary = Secondary,
        background = SurfaceLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        onBackground = OnSurfaceLight,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = Secondary,
        secondary = Primary,
        background = SurfaceDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        onBackground = OnSurfaceDark,
    )

@Composable
fun SmartNewsTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
