    package com.example.uas_project.ui.theme

    import android.app.Activity
    import android.os.Build
    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.darkColorScheme
    import androidx.compose.material3.dynamicDarkColorScheme
    import androidx.compose.material3.dynamicLightColorScheme
    import androidx.compose.material3.lightColorScheme
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.platform.LocalContext

    private val LightColorScheme = lightColorScheme(
        primary = VibrantBlue,
        secondary = VibrantOrange,
        tertiary = VibrantPurple,
        error = VibrantRed,

        background = SoftBackground,
        surface = CardLight,

        onPrimary = White,
        onSecondary = White,
        onBackground = Grey,
        onSurface = Grey
    )

    private val DarkColorScheme = darkColorScheme(
        primary = LightBlue,
        secondary = LightOrange,
        tertiary = LightPurple,
        error = VibrantRed,

        background = DeepGrey,
        surface = Grey,

        onPrimary = DeepGrey,
        onSecondary = DeepGrey,
        onBackground = White,
        onSurface = White
    )

    @Composable
    fun Uas_projectTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,
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