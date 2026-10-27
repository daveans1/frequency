package com.david.frequency.ui.theme

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import com.materialkolor.score.Score

import androidx.compose.runtime.getValue
import com.david.frequency.constants.SelectedFontKey
import com.david.frequency.constants.AppFont
import com.david.frequency.utils.rememberPreference
import androidx.compose.ui.text.font.FontFamily

// Base fallback for legacy arguments
val DefaultThemeColor = FrequencyColors.ElectricAqua
val FrequencySignatureColor = FrequencyColors.ElectricAqua

// Force Deep Abyss & Cyber-Cyan on all Material surfaces
private val FrequencyColorScheme = darkColorScheme(
    primary = FrequencyColors.ElectricAqua,
    onPrimary = FrequencyColors.DeepAbyss,
    primaryContainer = FrequencyColors.AcousticGlass,
    onPrimaryContainer = FrequencyColors.ElectricAqua,
    
    secondary = FrequencyColors.SonicCyan,
    onSecondary = FrequencyColors.DeepAbyss,
    secondaryContainer = FrequencyColors.AcousticGlassSubtle,
    onSecondaryContainer = FrequencyColors.SonicCyan,
    
    tertiary = FrequencyColors.DeepResonance,
    onTertiary = FrequencyColors.SonicWhite,
    
    background = FrequencyColors.DeepAbyss,
    onBackground = FrequencyColors.SonicWhite,
    
    surface = FrequencyColors.DeepAbyss,
    onSurface = FrequencyColors.SonicWhite,
    
    surfaceVariant = FrequencyColors.MidnightSurface,
    onSurfaceVariant = FrequencyColors.SonicMuted,
    
    outline = FrequencyColors.SonicSubtle
)

@Composable
fun vivimusicTheme(
    darkTheme: Boolean = true, // Force Dark
    pureBlack: Boolean = true, // Ignore, handled by DeepAbyss
    themeColor: Color = DefaultThemeColor,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val selectedFontValue by rememberPreference(SelectedFontKey, AppFont.SYSTEM.value)

    val brandFont = remember(selectedFontValue) {
        when (AppFont.fromValue(selectedFontValue)) {
            AppFont.SYSTEM -> FontFamily.Default
            AppFont.GOOGLE_SANS -> GoogleSansFontFamily
            AppFont.SANS_FLEX -> SansFlexFontFamily
            AppFont.OUTFIT -> OutfitFontFamily
            AppFont.PLUS_JAKARTA_SANS -> PlusJakartaSansFontFamily
        }
    }

    val typography = remember(brandFont) {
        getTypography(brandFont = brandFont, plainFont = brandFont)
    }

    MaterialExpressiveTheme(
        colorScheme = FrequencyColorScheme,
        typography = typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}

fun Bitmap.extractThemeColor(): Color {
    val colorsToPopulation = Palette.from(this)
        .maximumColorCount(8)
        .generate()
        .swatches
        .associate { it.rgb to it.population }
    val rankedColors = Score.score(colorsToPopulation)
    return Color(rankedColors.first())
}

fun Bitmap.extractGradientColors(): List<Color> {
    val extractedColors = Palette.from(this)
        .maximumColorCount(64)
        .generate()
        .swatches
        .associate { it.rgb to it.population }

    val orderedColors = Score.score(extractedColors, 2, 0xff4285f4.toInt(), true)
        .sortedByDescending { Color(it).luminance() }

    return if (orderedColors.size >= 2)
        listOf(Color(orderedColors[0]), Color(orderedColors[1]))
    else
        listOf(Color(0xFF595959), Color(0xFF0D0D0D))
}

fun ColorScheme.pureBlack(apply: Boolean) = this // No-op, DeepAbyss is default

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
