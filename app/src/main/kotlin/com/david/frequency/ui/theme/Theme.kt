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

import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

// Base fallback for legacy arguments
val DefaultThemeColor = FrequencyColors.ElectricAqua
val FrequencySignatureColor = FrequencyColors.ElectricAqua

// Signature Frequency Light Color Scheme (Luminous Acoustic Porcelain)
private val FrequencyLightColorScheme = lightColorScheme(
    primary = FrequencyColors.ElectricTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0x2400897B),
    onPrimaryContainer = Color(0xFF003830),

    secondary = FrequencyColors.SonicAzure,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),

    tertiary = FrequencyColors.DeepResonance,
    onTertiary = Color.White,

    background = FrequencyColors.LuminousPorcelain,
    onBackground = FrequencyColors.ObsidianText,

    surface = FrequencyColors.LuminousSurface,
    onSurface = FrequencyColors.ObsidianText,

    surfaceVariant = FrequencyColors.AcousticGlassSubtleLight,
    onSurfaceVariant = FrequencyColors.SlateMuted,

    surfaceContainerLow = FrequencyColors.LuminousPorcelain,
    surfaceContainer = FrequencyColors.AcousticGlassSubtleLight,
    surfaceContainerHigh = Color(0xFFE2E8F0),
    surfaceContainerHighest = Color(0xFFCBD5E1),

    outline = Color(0xFFCBD5E1),
    outlineVariant = FrequencyColors.GlassBorderLight
)

// Signature Frequency Dark Color Scheme (Deep Abyss & Cyber-Cyan)
private val FrequencyDarkColorScheme = darkColorScheme(
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

    surfaceContainerLow = Color(0xFF0E1322),
    surfaceContainer = FrequencyColors.MidnightSurface,
    surfaceContainerHigh = FrequencyColors.AcousticGlass,
    surfaceContainerHighest = FrequencyColors.AcousticGlassSubtle,

    outline = FrequencyColors.SonicSubtle,
    outlineVariant = FrequencyColors.GlassBorder
)

@Composable
fun vivimusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    dynamicColor: Boolean = true,
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

    // Determine if system dynamic wallpaper colors should be used (Android 12+ and default color)
    val useSystemDynamicColor = dynamicColor && (themeColor == DefaultThemeColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)

    val baseColorScheme = when {
        useSystemDynamicColor -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeColor == DefaultThemeColor -> {
            if (darkTheme) FrequencyDarkColorScheme else FrequencyLightColorScheme
        }
        else -> {
            rememberDynamicColorScheme(
                seedColor = themeColor,
                isDark = darkTheme,
                specVersion = ColorSpec.SpecVersion.SPEC_2025,
                style = if (themeColor.toArgb() == 0xFF000000.toInt()) PaletteStyle.Monochrome else PaletteStyle.TonalSpot
            )
        }
    }

    val colorScheme = remember(baseColorScheme, pureBlack, darkTheme) {
        if (darkTheme && pureBlack) {
            baseColorScheme.pureBlack(true)
        } else {
            baseColorScheme
        }
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
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

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = Color.Black,
        background = Color.Black,
        surfaceContainerLow = Color.Black,
        surfaceContainer = Color.Black,
        surfaceContainerLowest = Color.Black
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
