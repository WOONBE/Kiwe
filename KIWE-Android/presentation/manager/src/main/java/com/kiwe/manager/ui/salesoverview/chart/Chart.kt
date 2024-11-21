/*
 * Copyright 2024 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiwe.manager.ui.salesoverview.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
internal fun Chart2(
    modifier: Modifier,
    totalPriceLast6Months: Map<String, Int>,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val priceHistory = totalPriceLast6Months.map { it.key }.reversed()
    val average = totalPriceLast6Months.values.sum() / totalPriceLast6Months.size
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                /* Learn more:
                https://patrykandpatrick.com/vico/wiki/cartesian-charts/layers/column-layer#data. */
                columnSeries { series(totalPriceLast6Months.map { it.value }.reversed()) }
            }
        }
    }

    ComposeChart2(modelProducer, modifier, priceHistory, average)
}

@Composable
private fun ComposeChart2(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
    priceHistory: List<String>,
    average: Int,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            color = Color(0xffff5500),
                            thickness = 8.dp,
                            shape = CorneredShape.rounded(allPercent = 40),
                        ),
                    ),
                ),
                startAxis =
                    VerticalAxis.rememberStart(
                        itemPlacer =
                            VerticalAxis.ItemPlacer.count({ _ ->
                                10
                            }),
                        valueFormatter = { c, value, _ ->
                            String.format(Locale.getDefault(), "%,d", value.toInt())
                        },
                        guideline = null,
                        tick = null,
                        label =
                            TextComponent(
                                textSizeSp = 15F,
                            ),
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        guideline = null,
                        tick = null,
                        valueFormatter = { _, value, _ ->
                            priceHistory[value.toInt()]
                        },
                        label =
                            TextComponent(
                                textSizeSp = 15F,
                            ),
                        itemPlacer =
                            remember {
                                HorizontalAxis.ItemPlacer.aligned(spacing = 1, addExtremeLabelPadding = true)
                            },
                    ),
                marker = rememberMarker(),
                decorations = listOf(rememberComposeHorizontalLine(average)),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
private fun rememberComposeHorizontalLine(average: Int): HorizontalLine {
    val color = Color(HORIZONTAL_LINE_COLOR)
    val line = rememberLineComponent(color, HORIZONTAL_LINE_THICKNESS_DP.dp)
    val labelComponent =
        rememberTextComponent(
            margins = dimensions(HORIZONTAL_LINE_LABEL_MARGIN_DP.dp),
            padding =
                dimensions(
                    HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP.dp,
                    HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP.dp,
                ),
            background = shapeComponent(color, CorneredShape.Pill),
        )
    return remember { HorizontalLine({ average.toDouble() }, line, labelComponent) }
}

private const val HORIZONTAL_LINE_COLOR = -2893786
private const val HORIZONTAL_LINE_THICKNESS_DP = 2f
private const val HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP = 8f
private const val HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP = 2f
private const val HORIZONTAL_LINE_LABEL_MARGIN_DP = 4f
