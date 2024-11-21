package com.kiwe.manager.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kiwe.manager.R
import com.kiwe.manager.ui.theme.Typography
import java.util.Locale

@Composable
fun CenterAlignedTable(data: List<List<String>>) {
    LazyColumn(
        modifier =
            Modifier
                .height(
                    LocalConfiguration.current.screenHeightDp.dp * 0.6F,
                ).padding(horizontal = 15.dp),
    ) {
        items(1) {
            TableHeader(data[0])
            HorizontalDivider(thickness = 2.dp, color = Color.Black)
        }
        items(data.drop(1).size) {
            TableBody(data.drop(1)[it])
            HorizontalDivider(thickness = 0.5.dp, color = Color.Black)
        }
    }
}

@Composable
fun TableHeader(tableHeader: List<String>) {
    Row {
        tableHeader.forEachIndexed { index, text ->
            when (index) {
                0 -> {
                    Text(
                        text = text,
                        modifier =
                            Modifier
                                .weight(2f)
                                .padding(4.dp),
                        color = Color.Black,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Start,
                    )
                }

                1 -> {
                    Text(
                        text = text,
                        modifier =
                            Modifier
                                .weight(8f)
                                .padding(4.dp),
                        color = Color.Black,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Start,
                    )
                }

                else -> {
                    Text(
                        text = text,
                        modifier =
                            Modifier
                                .weight(2f)
                                .padding(4.dp),
                        color = Color.Black,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}

@Composable
fun TableBody(tableBody: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tableBody.forEachIndexed { index, data ->
            when (index) {
                0 -> {
                    val parts = data.split("\n")
                    val title = parts.firstOrNull() ?: ""
                    val option = parts.drop(1).joinToString("\n")
                    Text(
                        text =
                            buildAnnotatedString {
                                withStyle(
                                    style =
                                        SpanStyle(
                                            color = Color.Black,
                                            fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                                            fontSize = 16.sp,
                                            letterSpacing = (-0.03).em,
                                        ),
                                ) {
                                    append(title)
                                }
                                if (option.isNotEmpty()) {
                                    append("\n")
                                }
                                withStyle(
                                    style =
                                        SpanStyle(
                                            color = Color.Black,
                                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            fontSize = 12.sp,
                                            letterSpacing = (-0.03).em,
                                        ),
                                ) {
                                    append(option)
                                }
                            },
                        modifier =
                            Modifier
                                .weight(2f)
                                .padding(8.dp),
                        color = Color.Black,
                        style = Typography.labelSmall,
                        textAlign = TextAlign.Start,
                    )
                }

                2 -> {
                    Text(
                        text =
                            String.format(Locale.getDefault(), "%,d", data.toInt()),
                        modifier =
                            Modifier
                                .weight(2F)
                                .padding(8.dp),
                        color = Color.Black,
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Start,
                    )
                }

                else -> {
                    Text(
                        text = data,
                        modifier =
                            Modifier
                                .weight(8f)
                                .padding(8.dp),
                        color = Color.Black,
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}
