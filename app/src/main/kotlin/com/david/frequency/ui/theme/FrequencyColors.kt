package com.david.frequency.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
object FrequencyColors {
    // 0. Base Surfaces
    val DeepAbyss = Color(0xFF0A0E1A)            // Base 0 - Pure midnight background
    val MidnightSurface = Color(0xFF121829)      // Base 1 - Elevated surfaces/cards
    val AcousticGlass = Color(0xFF161E38)        // Base 2 - Translucent frosted glass containers
    val AcousticGlassSubtle = Color(0xFF1A2238)  // Base 3 - Micro-elevated elements
    
    // 1. Sonic Accents (From Brand Logo)
    val ElectricAqua = Color(0xFF20E0B0)         // Primary Accent - Playhead, indicators, primary buttons
    val SonicCyan = Color(0xFF3DF5FC)            // Secondary Accent - Peaks, radar ripple, audio badges
    val DeepResonance = Color(0xFF6C5CE7)        // Auxiliary - Ambient glow, live radar
    val FrequencyBlue = Color(0xFF1E88E5)        // Auxiliary - Deep audio waves
    
    // 2. Borders and Glows
    val GlassBorder = Color(0x2520E0B0)          // 15% Cyan glass edge sheen
    val GlassBorderActive = Color(0x8020E0B0)    // 50% Active edge sheen
    val GlowCyan = Color(0x403DF5FC)             // 25% Ambient cyan blur
    val GlowAqua = Color(0x4020E0B0)             // 25% Ambient aqua blur
    
    // 3. High-Contrast Typography
    val SonicWhite = Color(0xFFF5F7FF)           // 98% Primary text & titles
    val SonicMuted = Color(0xFF8F9CAE)           // 70% Secondary artist/subtitles
    val SonicSubtle = Color(0xFF5A667A)          // 45% Metadata & bitrate stamps
    
    // 4. Acoustic Gradients
    val GlassGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xCC161E38),
            Color(0x99121829)
        )
    )

    val RadarPulseGradient = Brush.radialGradient(
        colors = listOf(
            SonicCyan.copy(alpha = 0.6f),
            ElectricAqua.copy(alpha = 0.2f),
            Color.Transparent
        )
    )

    val WaveformBarGradient = Brush.verticalGradient(
        colors = listOf(
            SonicCyan,
            ElectricAqua
        )
    )
}
