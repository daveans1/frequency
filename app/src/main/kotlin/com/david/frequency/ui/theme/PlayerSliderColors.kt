/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.david.frequency.constants.PlayerBackgroundStyle

/**
 * Player slider color configuration for consistent styling across all slider types
 * 
 * This object provides standardized color schemes for Default, Squiggly, and Slim sliders
 * used in the music player interface, ensuring visual consistency and proper contrast.
 */
import com.david.frequency.ui.theme.FrequencyColors

object PlayerSliderColors {

    @Composable
    fun getSliderColors(
        activeColor: Color,
        playerBackground: PlayerBackgroundStyle,
        useDarkTheme: Boolean
    ): SliderColors {
        val effectiveActive = when (playerBackground) {
            PlayerBackgroundStyle.DEFAULT -> FrequencyColors.SonicCyan
            else -> activeColor
        }
        val inactiveTrackColor = when (playerBackground) {
            PlayerBackgroundStyle.DEFAULT -> {
                if (useDarkTheme) {
                    FrequencyColors.AcousticGlassSubtle
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
            }
            PlayerBackgroundStyle.BLUR, PlayerBackgroundStyle.GRADIENT, PlayerBackgroundStyle.GLOW_ANIMATED, PlayerBackgroundStyle.APPLE_MUSIC, PlayerBackgroundStyle.LIVE_MESH -> {
                Color.White.copy(alpha = 0.4f)
            }
        }
        
        return SliderDefaults.colors(
            activeTrackColor = effectiveActive,
            activeTickColor = effectiveActive,
            thumbColor = effectiveActive,
            inactiveTrackColor = inactiveTrackColor,
            disabledActiveTrackColor = effectiveActive,
            disabledInactiveTrackColor = inactiveTrackColor,
            disabledThumbColor = effectiveActive
        )
    }
}
