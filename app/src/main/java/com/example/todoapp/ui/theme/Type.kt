package com.example.todoapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.todoapp.R

val Nunito = FontFamily(
    Font(R.font.nunito_regular)
)

val RobotoMono = FontFamily(
    Font(R.font.robotomono_medium, FontWeight.SemiBold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)