package bd.com.albin.news.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bd.com.albin.news.R

val NunitoSans = FontFamily(Font(R.font.nunito_sans))
val SourceCodePro = FontFamily(Font(R.font.source_code_pro))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SourceCodePro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ), titleLarge = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ), bodyMedium = TextStyle(
        fontFamily = SourceCodePro,
    ), titleMedium = TextStyle(
        fontFamily = NunitoSans,
    )
)