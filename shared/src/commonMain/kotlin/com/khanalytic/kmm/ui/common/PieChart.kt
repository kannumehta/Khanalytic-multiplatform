package com.khanalytic.kmm.ui.common
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.khanalytic.kmm.ui.common.MathUtils.percentage
import com.khanalytic.kmm.ui.common.MathUtils.round
import com.khanalytic.kmm.ui.common.MathUtils.toPercentageString

data class PieChartEntry(val partName: String, val data: Float, val color: Color)

@Composable
fun PieChart(
    data: List<PieChartEntry>,
    radius: Float = 150f,
    textColor: Color = textColor()
) {
    val totalValue = data.map { it.data }.sum()
    var startAngle = 0f
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier.size((2 * radius).dp)
    ) {
        for (entry in data) {
            val sweepAngle = if (totalValue > 0f)  {
                (entry.data / totalValue) * 360f
            } else { 0f }

            drawArc(
                color = entry.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                style = Fill
            )
            startAngle += sweepAngle
        }

        startAngle = 0f
        for (entry in data) {
            val sweepAngle = if (totalValue > 0f)  {
                (entry.data / totalValue) * 360f
            } else { 0f }

            val startAngleRad = startAngle / 180f * kotlin.math.PI.toFloat()
            val sweepAngleRad = sweepAngle / 180f * kotlin.math.PI.toFloat()
            val endAngleRad = startAngleRad + sweepAngleRad

            val midAngleRad = (startAngleRad + endAngleRad) / 2f
            val startX = radius * kotlin.math.cos(midAngleRad)
            val startY = radius * kotlin.math.sin(midAngleRad)

            val label = entry.partName
            val labelSize = textMeasurer.measure(label, TextStyle.Default).size
            val labelX = (radius + (startX/1.3)).dp.toPx() - labelSize.width/2
            val labelY = (radius + (startY/1.3)).dp.toPx() - labelSize.height/2
            val offset = Offset(labelX, labelY)

            drawText(
                textMeasurer = textMeasurer,
                text = label,
                topLeft = offset,
                style = TextStyle.Default.copy(color = textColor)
            )

            val valueLabel = percentage(entry.data, totalValue).toPercentageString()
            val valueLabelSize = textMeasurer.measure(valueLabel, TextStyle.Default).size
            val valueLabelX = (radius + (startX/1.3)).dp.toPx() - valueLabelSize.width/2
            val valueLabelY = labelY + labelSize.height
            val valueOffset = Offset(valueLabelX, valueLabelY)
            drawText(
                textMeasurer = textMeasurer,
                text = valueLabel,
                topLeft = valueOffset,
                style = TextStyle.Default.copy(color = textColor)
            )

            startAngle += sweepAngle
        }
    }
}

@Composable
fun DrawText(
    textMeasurer: TextMeasurer,
    text: String,
    style: TextStyle,
    topLeft: Offset
) {
    val textLayoutResult = textMeasurer.measure(text, style)
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawText(
            textMeasurer = textMeasurer,
            text = text,
            style = style,
            topLeft = Offset(
                topLeft.x - textLayoutResult.size.width / 2,
                topLeft.y - textLayoutResult.size.height / 2
            )
        )
    }
}