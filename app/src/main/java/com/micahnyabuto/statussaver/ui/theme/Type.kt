package com.micahnyabuto.statussaver.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.micahnyabuto.statussaver.R

val productSan = FontFamily(
    Font(R.font.product_sans_light, FontWeight.Light),
    Font(R.font.product_sans_regular, FontWeight.Normal),
    Font(R.font.product_sans_medium, FontWeight.Medium),
    Font(R.font.product_sans_thin, FontWeight.Thin),
    Font(R.font.product_sans_bold, FontWeight.Bold),
    Font(R.font.product_sans_black, FontWeight.Black)
    )

val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(
            fontFamily = productSan
        ),
        displayMedium = displayMedium.copy(
            fontFamily = productSan
        ),
        displaySmall = displaySmall.copy(
            fontFamily = productSan
        ),
        headlineLarge = headlineLarge.copy(
            fontFamily = productSan
        ),
        headlineMedium = headlineMedium.copy(
            fontFamily = productSan
        ),
        headlineSmall = headlineSmall.copy(
            fontFamily = productSan
        ),
        titleLarge = titleLarge.copy(
            fontFamily = productSan,
            fontWeight = FontWeight.Bold
        ),
        titleMedium = titleMedium.copy(
            fontFamily = productSan,
            fontWeight = FontWeight.Bold
        ),
        titleSmall = titleSmall.copy(
            fontFamily = productSan,
            fontWeight = FontWeight.Bold
        ),
        bodyLarge = bodyLarge.copy(
            fontFamily = productSan
        ),
        bodyMedium = bodyMedium.copy(
            fontFamily = productSan
        ),
        bodySmall = bodySmall.copy(
            fontFamily = productSan
        ),
        labelLarge = labelLarge.copy(
            fontFamily = productSan
        ),
        labelMedium = labelMedium.copy(
            fontFamily = productSan
        ),
        labelSmall = labelSmall.copy(
            fontFamily = productSan
        ),
    )
}