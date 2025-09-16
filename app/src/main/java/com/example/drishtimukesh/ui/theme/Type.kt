package com.example.drishtimukesh.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.drishtimukesh.R

// Set of Material typography styles to start with
// Font Families
val Poppins = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

val Montserrat = FontFamily(
//    Font(R.font., FontWeight.SemiBold),
    Font(R.font.monteserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_regular, FontWeight.Normal)

)

val Typography = Typography(
    // Search
    titleMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Color(0xFF999999).copy(alpha = 0.82f)
    ),
    // Course
    bodyLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Color(0xFF1B1126).copy(alpha = 0.82f)
    ),
    // Ready to start...
    headlineLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 43.6.sp,
        lineHeight = 54.sp,
        color = Color(0xFF221730)
    ),
    // Join our courses..
    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Light,
        fontSize = 25.9.sp,
        lineHeight = 36.sp,
        color = Color(0xFF221730).copy(alpha = 0.65f)

//        color = TextHeading.copy(alpha = 0.65f)
    ),
    // Explore our exclusively..
    labelSmall = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.1.sp,
        lineHeight = 19.sp,
        color = Color(0xFF221730)
//        color = TextHeading
    )
)
