package com.david.frequency.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.david.frequency.ui.theme.FrequencyColors

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance

/**
 * Applies an Acoustic Frosted Glass surface with a subtle edge sheen.
 */
fun Modifier.acousticGlass(
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.82f,
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(cornerRadius)
): Modifier {
    val bg = if (backgroundColor != Color.Unspecified) backgroundColor else FrequencyColors.AcousticGlass.copy(alpha = alpha)
    val border = if (borderColor != Color.Unspecified) borderColor else FrequencyColors.GlassBorder
    return this
        .clip(shape)
        .background(
            color = bg,
            shape = shape
        )
        .border(
            width = borderWidth,
            color = border,
            shape = shape
        )
}

/**
 * Theme-aware acoustic frosted glass that automatically adapts to Light Mode and Dark Mode.
 */
@Composable
fun Modifier.adaptiveAcousticGlass(
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.88f,
    borderWidth: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(cornerRadius)
): Modifier {
    val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5f
    val bg = if (isLight) FrequencyColors.AcousticGlassLight.copy(alpha = alpha) else FrequencyColors.AcousticGlass.copy(alpha = alpha)
    val border = if (isLight) FrequencyColors.GlassBorderLight else FrequencyColors.GlassBorder
    return this
        .clip(shape)
        .background(color = bg, shape = shape)
        .border(width = borderWidth, color = border, shape = shape)
}

/**
 * Neon glow radial effect behind components (e.g. for active playback indicators, playheads).
 */
fun Modifier.neonGlow(
    color: Color = FrequencyColors.ElectricAqua,
    radius: Dp = 12.dp,
    shape: Shape = RoundedCornerShape(50)
): Modifier = this.shadow(
    elevation = radius,
    shape = shape,
    ambientColor = color,
    spotColor = color
)

/**
 * Rhythmic 120-BPM sonic wave shimmer for loading skeletons.
 */
@Composable
fun Modifier.audioShimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "audioShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val shimmerColors = listOf(
        FrequencyColors.MidnightSurface,
        FrequencyColors.AcousticGlass,
        FrequencyColors.ElectricAqua.copy(alpha = 0.15f),
        FrequencyColors.AcousticGlass,
        FrequencyColors.MidnightSurface
    )

    return this.drawBehind {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 500f, translateAnim - 500f),
            end = Offset(translateAnim, translateAnim)
        )
        drawRect(brush = brush)
    }
}
