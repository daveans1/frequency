package com.david.frequency.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.frequency.ui.theme.FrequencyColors

/**
 * An illuminated hardware-style power pod switch with an integrated glowing neon LED dot.
 */
@Composable
fun IlluminatedPowerPod(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 2.dp,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset"
    )
    val trackBgColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF132D33) else FrequencyColors.MidnightSurface,
        animationSpec = tween(durationMillis = 200),
        label = "trackBgColor"
    )
    val trackBorderColor by animateColorAsState(
        targetValue = if (checked) FrequencyColors.ElectricAqua else FrequencyColors.AcousticGlassSubtle,
        animationSpec = tween(durationMillis = 200),
        label = "trackBorderColor"
    )

    Box(
        modifier = modifier
            .width(52.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(50))
            .background(trackBgColor)
            .border(1.dp, trackBorderColor, RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Thumb Knob
        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .size(26.dp)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF1B243B) else Color(0xFF263352))
                .border(
                    1.dp,
                    if (checked) FrequencyColors.SonicCyan else Color(0xFF3B4D75),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Illuminated Center LED Dot
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (checked) FrequencyColors.SonicCyan else Color(0xFF5A667A))
                    .then(
                        if (checked) Modifier.neonGlow(FrequencyColors.SonicCyan, 4.dp, CircleShape)
                        else Modifier
                    )
            )
        }
    }
}

/**
 * Audiophile Hardware Telemetry Strip showing system audio pipeline metrics.
 */
@Composable
fun AcousticTelemetryStrip(
    modifier: Modifier = Modifier,
    statusText: String = "Audio Engine: 320kbps Lossless Ready • Axion DSP Active"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .acousticGlass(cornerRadius = 14.dp, alpha = 0.65f)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pulsing Live Indicator Dot
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(FrequencyColors.SonicCyan)
                .neonGlow(FrequencyColors.SonicCyan, 6.dp, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = statusText,
            color = FrequencyColors.SonicMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 2x2 Bento Console Card for primary settings categories.
 */
@Composable
fun AcousticBentoCard(
    title: String,
    subtitle: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = FrequencyColors.ElectricAqua,
    trailingBadge: @Composable (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .acousticGlass(cornerRadius = 20.dp, alpha = 0.85f)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon in illuminated container
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                trailingBadge?.invoke()
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column {
                Text(
                    text = title,
                    color = FrequencyColors.SonicWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = FrequencyColors.SonicMuted,
                    fontSize = 12.sp,
                    maxLines = 2,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
