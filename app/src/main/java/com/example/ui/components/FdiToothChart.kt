package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DentalTealPrimary
import com.example.ui.theme.DentalWarningOrange
import com.example.ui.theme.Quad1Color
import com.example.ui.theme.Quad2Color
import com.example.ui.theme.Quad3Color
import com.example.ui.theme.Quad4Color

/**
 * Interactive FDI Dental Notation Chart (نظام ترقيم الأسنان العالمي FDI)
 * Displays standard dental arches with quadrants:
 * Q1 (18-11) | Q2 (21-28)
 * -----------------------
 * Q4 (48-41) | Q3 (31-38)
 */
@Composable
fun FdiToothChart(
    selectedTooth: String? = null,
    onToothSelected: ((String) -> Unit)? = null,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(if (compact) 8.dp else 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Midline label
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "يمين المريض (R)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    text = "مخطط الأسنان FDI العالمي",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "يسار المريض (L)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Upper Arch (Maxilla): Q1 (18..11) - Q2 (21..28)
            Text(
                text = "الفك العلوي (Maxilla)",
                style = MaterialTheme.typography.labelSmall,
                color = Quad1Color,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quadrant 1: 18, 17, 16, 15, 14, 13, 12, 11
                ToothQuadrantRow(
                    teeth = listOf("18", "17", "16", "15", "14", "13", "12", "11"),
                    selectedTooth = selectedTooth,
                    quadrantColor = Quad1Color,
                    compact = compact,
                    onToothSelected = onToothSelected
                )

                // Midline
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(if (compact) 28.dp else 36.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                )

                // Quadrant 2: 21, 22, 23, 24, 25, 26, 27, 28
                ToothQuadrantRow(
                    teeth = listOf("21", "22", "23", "24", "25", "26", "27", "28"),
                    selectedTooth = selectedTooth,
                    quadrantColor = Quad2Color,
                    compact = compact,
                    onToothSelected = onToothSelected
                )
            }

            // Occlusal plane line
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(2.dp)
                    .padding(vertical = 1.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )

            // Lower Arch (Mandible): Q4 (48..41) - Q3 (31..38)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quadrant 4: 48, 47, 46, 45, 44, 43, 42, 41
                ToothQuadrantRow(
                    teeth = listOf("48", "47", "46", "45", "44", "43", "42", "41"),
                    selectedTooth = selectedTooth,
                    quadrantColor = Quad4Color,
                    compact = compact,
                    onToothSelected = onToothSelected
                )

                // Midline
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(if (compact) 28.dp else 36.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                )

                // Quadrant 3: 31, 32, 33, 34, 35, 36, 37, 38
                ToothQuadrantRow(
                    teeth = listOf("31", "32", "33", "34", "35", "36", "37", "38"),
                    selectedTooth = selectedTooth,
                    quadrantColor = Quad3Color,
                    compact = compact,
                    onToothSelected = onToothSelected
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "الفك السفلي (Mandible)",
                style = MaterialTheme.typography.labelSmall,
                color = Quad4Color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ToothQuadrantRow(
    teeth: List<String>,
    selectedTooth: String?,
    quadrantColor: Color,
    compact: Boolean,
    onToothSelected: ((String) -> Unit)?
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(if (compact) 2.dp else 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        teeth.forEach { toothNumber ->
            val isSelected = selectedTooth == toothNumber
            ToothBadge(
                number = toothNumber,
                isSelected = isSelected,
                quadrantColor = quadrantColor,
                compact = compact,
                onClick = { onToothSelected?.invoke(toothNumber) }
            )
        }
    }
}

@Composable
fun ToothBadge(
    number: String,
    isSelected: Boolean,
    quadrantColor: Color,
    compact: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) DentalWarningOrange else MaterialTheme.colorScheme.surface,
        label = "tooth_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
        label = "tooth_text"
    )

    val size = if (compact) 20.dp else 26.dp
    val fontSize = if (compact) 8.sp else 10.sp

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 1.5.dp else 0.5.dp,
                color = if (isSelected) DentalWarningOrange else quadrantColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = fontSize,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
