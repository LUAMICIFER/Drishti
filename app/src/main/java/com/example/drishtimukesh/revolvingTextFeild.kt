package com.example.drishtimukesh

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun RevolvingDashedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    dashOnPx: Float = 800f,        // dash length (px)
    dashOffPx: Float = 24f,        // gap length (px)
    strokeWidthDp: Float = 2f,     // animated stroke width
    cornerRadiusDp: Float = 12f,   // match OutlinedTextField default-ish radius
    revolveMs: Int = 1200,         // speed of revolution
    baseBorderColor: Color = Color.Gray,   // faint static border
    dashColor: Color = Color.Black,        // active colour
    maxlines: Int = 1,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,   // ✅ added
    trailingIcon: @Composable (() -> Unit)? = null   // ✅ added
) {
    var isFocused by remember { mutableStateOf(false) }

    // Animate the dash phase only while focused
    val infinite = rememberInfiniteTransition(label = "dash")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = dashOnPx + dashOffPx, // one dash+gap cycle
        animationSpec = infiniteRepeatable(
            animation = tween(revolveMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    // Convert dp to px for stroke/corners
    val strokeWidthPx = with(LocalDensity.current) { strokeWidthDp.dp.toPx() }
    val cornerRadiusPx = with(LocalDensity.current) { cornerRadiusDp.dp.toPx() }

    Box(
        modifier = modifier.drawBehind {
            val pad = strokeWidthPx / 2f
            val w = size.width - pad * 2
            val h = size.height - pad * 2
            val topLeft = Offset(pad, pad)

            // 1) Base subtle border
            drawRoundRect(
                color = baseBorderColor,
                topLeft = topLeft,
                size = Size(w, h),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                style = Stroke(width = strokeWidthPx)
            )

            // 2) Animated dashed border (only when focused)
            if (isFocused) {
                drawRoundRect(
                    color = dashColor,
                    topLeft = topLeft,
                    size = Size(w, h),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                    style = Stroke(
                        width = strokeWidthPx,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(dashOnPx, dashOffPx),
                            phase = phase
                        )
                    )
                )
            }
        }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            label = label,
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            leadingIcon = leadingIcon,   // ✅ here
            trailingIcon = trailingIcon, // ✅ here
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(cornerRadiusDp.dp),
            maxLines = maxlines
        )
    }
}
