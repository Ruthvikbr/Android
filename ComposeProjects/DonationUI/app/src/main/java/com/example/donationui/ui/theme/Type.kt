package com.example.donationui.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.donationui.R

val fredoka = FontFamily(
    listOf(
        Font(R.font.fredoka_bold, FontWeight.Bold),
        Font(R.font.fredoka_light, FontWeight.Light),
        Font(R.font.fredoka_medium, FontWeight.Medium),
        Font(R.font.fredoka_regular, FontWeight.Normal),
        Font(R.font.fredoka_semi_bold, FontWeight.SemiBold),
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fredoka,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    h1 = TextStyle(
        fontFamily = fredoka,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    h2 = TextStyle(
        fontFamily = fredoka,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)