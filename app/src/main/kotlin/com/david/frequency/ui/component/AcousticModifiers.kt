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

/**
 * Applies an Acoustic Frosted Glass surface with a subtle cyan edge sheen.
 */
fun Modifier.acousticGlass(
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.82f,
    borderColor: Color = FrequencyColors.GlassBorder,
    borderWidth: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(cornerRadius)
): Modifier = this
    .clip(shape)
    .background(
        color = FrequencyColors.AcousticGlass.copy(alpha = alpha),
        shape = shape
    )
    .border(
        width = borderWidth,
        color = borderColor,
        shape = shape
    )

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
