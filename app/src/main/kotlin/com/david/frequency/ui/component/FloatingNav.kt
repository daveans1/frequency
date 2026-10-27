/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.david.frequency.ui.screens.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

import com.david.frequency.ui.theme.FrequencyColors

@Composable
fun FloatingNavigationBar(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    slimNav: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null,
    bottomInset: Dp = 0.dp
) {
    val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5f

    val containerColor = when {
        pureBlack -> Color.Black
        isLight -> FrequencyColors.AcousticGlassLight.copy(alpha = 0.94f)
        else -> FrequencyColors.MidnightSurface.copy(alpha = 0.90f)
    }

    val outlineColor = when {
        pureBlack -> Color(0xFF222222)
        isLight -> FrequencyColors.GlassBorderLight
        else -> FrequencyColors.GlassBorder
    }

    val shadowColor = when {
        pureBlack -> Color.Transparent
        isLight -> Color(0x180F172A)
        else -> FrequencyColors.ElectricAqua.copy(alpha = 0.25f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = bottomInset + 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Unified Sonic Dock: All navigation destinations gathered into a single floating capsule
        Row(
            modifier = Modifier
                .height(if (slimNav) 56.dp else 62.dp)
                .shadow(elevation = 12.dp, shape = CircleShape, spotColor = shadowColor, ambientColor = shadowColor)
                .background(containerColor, shape = CircleShape)
                .border(width = 1.dp, color = outlineColor, shape = CircleShape)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            navigationItems.forEach { screen ->
                val isSelected = remember(currentRoute, screen.route, navigationItems) {
                    isRouteSelected(currentRoute, screen.route, navigationItems)
                }
                FloatingNavItem(
                    screen = screen,
                    isSelected = isSelected,
                    onClick = { onItemClick(screen, isSelected) },
                    slimNav = slimNav,
                    isLight = isLight,
                    onSearchLongClick = if (screen == Screens.Search) onSearchLongClick else null
                )
            }
        }
    }
}

private fun isRouteSelected(currentRoute: String?, screenRoute: String, navigationItems: List<Screens>): Boolean {
    if (currentRoute == null) return false
    if (currentRoute == screenRoute) return true
    return navigationItems.any { it.route == screenRoute } && 
           currentRoute.startsWith("$screenRoute/")
}

@Composable
private fun FloatingNavItem(
    screen: Screens,
    isSelected: Boolean,
    onClick: () -> Unit,
    slimNav: Boolean,
    isLight: Boolean,
    modifier: Modifier = Modifier,
    onSearchLongClick: (() -> Unit)? = null
) {
    val haptics = LocalHapticFeedback.current
    val viewConfiguration = LocalViewConfiguration.current
    val interactionSource = remember { MutableInteractionSource() }

    val isSearchItem = screen == Screens.Search && onSearchLongClick != null
    if (isSearchItem) {
        LaunchedEffect(interactionSource) {
            var isLongClick = false
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        isLongClick = false
                        delay(viewConfiguration.longPressTimeoutMillis)
                        isLongClick = true
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSearchLongClick?.invoke()
                    }
                    is PressInteraction.Release -> {
                        if (!isLongClick) {
                            onClick()
                        }
                    }
                    is PressInteraction.Cancel -> {
                        isLongClick = false
                    }
                }
            }
        }
    }

    val iconRes = if (isSelected) screen.iconIdActive else screen.iconIdInactive
    val targetContentColor = when {
        isSelected && isLight -> FrequencyColors.ElectricTeal
        isSelected && !isLight -> FrequencyColors.SonicCyan
        !isSelected && isLight -> FrequencyColors.SlateMuted
        else -> FrequencyColors.SonicMuted
    }
    val contentColor by animateColorAsState(
        targetValue = targetContentColor,
        animationSpec = tween(durationMillis = 200),
        label = "contentColor"
    )

    val targetBgColor = when {
        isSelected && isLight -> FrequencyColors.ElectricTeal.copy(alpha = 0.14f)
        isSelected && !isLight -> FrequencyColors.ElectricAqua.copy(alpha = 0.22f)
        else -> Color.Transparent
    }
    val backgroundColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 200),
        label = "backgroundColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = {
                    if (!isSearchItem) {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick()
                    }
                }
            )
            .padding(horizontal = if (slimNav) 10.dp else 14.dp, vertical = if (slimNav) 8.dp else 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(screen.titleId),
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            AnimatedVisibility(
                visible = !slimNav && isSelected,
                enter = fadeIn(animationSpec = tween(150)) + expandHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(animationSpec = tween(100)) + shrinkHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
            ) {
                Text(
                    text = stringResource(screen.titleId),
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}
