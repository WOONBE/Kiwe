package com.kiwe.kiosk.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KiweBlack1
import com.kiwe.kiosk.ui.theme.KiweOrange1
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.ui.theme.fixedWidthStyle

/**
 * TODO
 *
 * @param modifier
 * @param fullText : 전체 텍스트
 * @param keywords : 강조 키워드
 * @param brushFlag : brush 여부, true -> brush on | false -> brush off
 * @param boldStyle : 키워드에 적용될 스타일
 * @param normalStyle : 일반 텍스트에 적용될 스타일
 * @param alignStyle : 정렬 방식
 */

@Composable
fun BoldTextWithKeywords(
    modifier: Modifier = Modifier,
    fullText: String,
    keywords: List<String>,
    brushFlag: List<Boolean>,
    boldStyle: TextStyle, // 키워드에 적용될 스타일
    normalStyle: TextStyle, // 일반 텍스트에 적용될 스타일,
    alignStyle: TextAlign? = TextAlign.Start,
    textColor: Color? = KiweBlack1,
) {
    val annotatedText =
        buildAnnotatedString {
            var currentIndex = 0
            keywords.forEachIndexed { index, keyword ->
                val keywordIndex = fullText.indexOf(keyword, currentIndex)
                if (keywordIndex >= 0) {
                    // keyword 이외의 텍스트
                    withStyle(
                        style =
                            SpanStyle(
                                fontFamily =
                                    FontFamily(
                                        Font(
                                            if (textColor == null) R.font.pretendard_regular else R.font.pretendard_bold,
                                        ),
                                    ),
                                fontWeight = normalStyle.fontWeight ?: FontWeight.Normal,
                                fontSize = normalStyle.fontSize,
                                color = normalStyle.color,
                                letterSpacing = normalStyle.letterSpacing,
                            ),
                    ) {
                        append(fullText.substring(currentIndex, keywordIndex))
                    }
                    // keyword 텍스트에 원하는 스타일 적용
                    withStyle(
                        style =
                            SpanStyle(
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                color = textColor?.takeIf { brushFlag[index] } ?: Color.Unspecified,
                                fontSize = boldStyle.fontSize,
                                letterSpacing = boldStyle.letterSpacing,
                            ),
                    ) {
                        append(keyword)
                    }
                    currentIndex = keywordIndex + keyword.length
                }
            }
            // 마지막 남은 텍스트 처리
            if (currentIndex < fullText.length) {
                withStyle(
                    style =
                        SpanStyle(
                            fontFamily =
                                FontFamily(
                                    Font(
                                        if (textColor == null) R.font.pretendard_regular else R.font.pretendard_bold,
                                    ),
                                ),
                            fontWeight = normalStyle.fontWeight ?: FontWeight.Normal,
                            fontSize = normalStyle.fontSize,
                            color = normalStyle.color,
                            letterSpacing = normalStyle.letterSpacing,
                        ),
                ) {
                    append(fullText.substring(currentIndex))
                }
            }
        }

    Text(
        modifier = modifier,
        text = annotatedText,
        textAlign = alignStyle,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBoldTextWithKeywords1() {
    KIWEAndroidTheme {
        val text = "어디에서 이용하시겠습니까?"
        val boldKeywords = listOf("어디", "이용")
        val brushFlag = listOf(true, true)
        val textColor = KiweOrange1
        BoldTextWithKeywords(
            modifier = Modifier,
            fullText = text,
            keywords = boldKeywords,
            brushFlag,
            boldStyle = Typography.titleSmall.copy(fontSize = 16.sp),
            normalStyle = Typography.titleSmall.copy(fontSize = 16.sp),
            textColor = textColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoldTextWithKeywords2() {
    KIWEAndroidTheme {
        val price = 4500
        val priceText = String.format("%,d", price)
        val text = "${priceText}${stringResource(id = R.string.kiwe_won)}"
        val boldKeywords = listOf(priceText)
        val brushFlag = listOf(true)
        BoldTextWithKeywords(
            modifier = Modifier,
            fullText = text,
            keywords = boldKeywords,
            brushFlag,
            boldStyle = Typography.fixedWidthStyle.copy(fontSize = 16.sp),
            normalStyle = Typography.labelMedium.copy(fontSize = 16.sp),
            textColor = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoldTextWithKeywords3() {
    KIWEAndroidTheme {
        val price = 1110
        val priceText = String.format("%,d", price)
        val text = "${priceText}${stringResource(id = R.string.kiwe_won)}"
        val boldKeywords = listOf(priceText)
        val brushFlag = listOf(true)
        BoldTextWithKeywords(
            modifier = Modifier,
            fullText = text,
            keywords = boldKeywords,
            brushFlag,
            boldStyle = Typography.fixedWidthStyle.copy(fontSize = 16.sp, fontFeatureSettings = "tnum"),
            normalStyle = Typography.labelMedium.copy(fontSize = 16.sp, fontFeatureSettings = "tnum"),
            textColor = null,
        )
    }
}