package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DentalCase
import com.example.model.DentalSpecialty
import com.example.ui.DentalUiState
import com.example.ui.DentalViewModel
import com.example.ui.components.FdiToothChart
import com.example.ui.theme.DentalBlueSecondary
import com.example.ui.theme.DentalSuccessGreen
import com.example.ui.theme.DentalTealPrimary
import com.example.ui.theme.DentalWarningOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaseSimulatorScreen(
    state: DentalUiState,
    viewModel: DentalViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    val currentCase = state.currentSimulatorCase

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Generator Controls Header Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DentalTealPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = DentalTealPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "مولّد الحالات السريرية",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "اختر المعايير أو ولد حالة عشوائية فوراً",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.generateNewSimulatorCase() },
                        modifier = Modifier.testTag("generate_case_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DentalTealPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "توليد حالة",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("توليد حالة", fontWeight = FontWeight.Bold)
                    }
                }

                // Filter chips: Specialty
                Text(
                    text = "التخصص:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip(
                        selected = state.simulatorSpecialtyFilter == "all",
                        onClick = { viewModel.setSimulatorSpecialtyFilter("all") },
                        label = { Text("جميع التخصصات", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DentalTealPrimary.copy(alpha = 0.2f),
                            selectedLabelColor = DentalTealPrimary
                        )
                    )
                    DentalSpecialty.entries.filter { it != DentalSpecialty.ALL }.forEach { spec ->
                        FilterChip(
                            selected = state.simulatorSpecialtyFilter == spec.id,
                            onClick = { viewModel.setSimulatorSpecialtyFilter(spec.id) },
                            label = { Text(spec.arabicName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DentalTealPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = DentalTealPrimary
                            )
                        )
                    }
                }

                // Filter chips: Difficulty
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المستوى:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    listOf("all" to "الكل", "سهلة" to "سهلة", "متوسطة" to "متوسطة", "صعبة" to "صعبة").forEach { (key, label) ->
                        FilterChip(
                            selected = state.simulatorDifficultyFilter == key,
                            onClick = { viewModel.setSimulatorDifficultyFilter(key) },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        if (currentCase != null) {
            // Case Presentation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Title and Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "حالة سريرية #${currentCase.id}",
                                style = MaterialTheme.typography.labelMedium,
                                color = DentalTealPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentCase.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    noteText = currentCase.userNotes
                                    showNoteDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = "ملاحظات الطالب",
                                    tint = if (currentCase.userNotes.isNotEmpty()) DentalTealPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleFavorite(currentCase) },
                                modifier = Modifier.testTag("bookmark_button")
                            ) {
                                Icon(
                                    imageVector = if (currentCase.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "حفظ الحالة",
                                    tint = if (currentCase.isFavorite) DentalWarningOrange else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badges: Specialty, Age, Tooth, Difficulty
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BadgeChip(
                            text = currentCase.specLabel,
                            bgColor = DentalTealPrimary.copy(alpha = 0.12f),
                            textColor = DentalTealPrimary
                        )
                        BadgeChip(
                            text = "العمر: ${currentCase.patientAge} سنة (${currentCase.ageGroupLabel})",
                            bgColor = DentalBlueSecondary.copy(alpha = 0.12f),
                            textColor = DentalBlueSecondary
                        )
                        BadgeChip(
                            text = "السن FDI: #${currentCase.tooth}",
                            bgColor = DentalWarningOrange.copy(alpha = 0.12f),
                            textColor = DentalWarningOrange
                        )
                        BadgeChip(
                            text = "صعوبة: ${currentCase.diff}",
                            bgColor = when (currentCase.diff) {
                                "سهلة" -> DentalSuccessGreen.copy(alpha = 0.12f)
                                "صعبة" -> Color(0xFFD32F2F).copy(alpha = 0.12f)
                                else -> DentalWarningOrange.copy(alpha = 0.12f)
                            },
                            textColor = when (currentCase.diff) {
                                "سهلة" -> DentalSuccessGreen
                                "صعبة" -> Color(0xFFD32F2F)
                                else -> DentalWarningOrange
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Patient History / Summary Callout
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = DentalBlueSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "القصة السريرية والفحص (Clinical Scenario):",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DentalBlueSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentCase.summary,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "موضع السن: ${currentCase.toothNameArabic} • ${currentCase.toothNameEnglish}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // FDI Tooth Locator Widget
                    Text(
                        text = "موقع السن على مخطط FDI (مضاء بالبرتقالي):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FdiToothChart(
                        selectedTooth = currentCase.tooth,
                        compact = true
                    )
                }
            }

            // Student Diagnosis Challenge (التشخيص التفاعلي)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = DentalWarningOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تحدي التشخيص السريري",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "بناءً على معطيات الحالة، ما هو تشخيصك الأرجح؟",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 Multiple Choice Options
                    state.simulatorGuessOptions.forEachIndexed { index, option ->
                        val isSelected = state.simulatorSelectedGuessIndex == index
                        val isCorrectAnswer = option == currentCase.title
                        val showCorrectness = state.simulatorSelectedGuessIndex != null

                        val borderColor = when {
                            showCorrectness && isCorrectAnswer -> DentalSuccessGreen
                            showCorrectness && isSelected && !isCorrectAnswer -> Color(0xFFD32F2F)
                            isSelected -> DentalTealPrimary
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        }

                        val bgColor = when {
                            showCorrectness && isCorrectAnswer -> DentalSuccessGreen.copy(alpha = 0.15f)
                            showCorrectness && isSelected && !isCorrectAnswer -> Color(0xFFD32F2F).copy(alpha = 0.12f)
                            isSelected -> DentalTealPrimary.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.submitSimulatorGuess(index)
                                },
                            color = bgColor
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(borderColor.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ('A'.code + index).toChar().toString(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = borderColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected || (showCorrectness && isCorrectAnswer)) FontWeight.Bold else FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                if (showCorrectness && isCorrectAnswer) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "صحيح",
                                        tint = DentalSuccessGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (showCorrectness && isSelected && !isCorrectAnswer) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "خطأ",
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    state.simulatorGuessFeedback?.let { feedback ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = feedback,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (feedback.contains("ممتاز")) DentalSuccessGreen else DentalWarningOrange
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reveal toggle buttons if user wants to inspect without selecting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.toggleRevealDiagnosis() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (state.isDiagnosisRevealed) "إخفاء التشخيص" else "كشف التشخيص")
                        }
                        OutlinedButton(
                            onClick = { viewModel.toggleRevealPlan() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (state.isPlanRevealed) "إخفاء الخطة" else "كشف خطة العلاج")
                        }
                    }
                }
            }

            // Treatment Plan Section
            AnimatedVisibility(visible = state.isPlanRevealed, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DentalSuccessGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = DentalSuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "خطة العلاج السريرية الموصى بها (Treatment Plan)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DentalSuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        currentCase.plan.forEachIndexed { pIdx, step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(DentalTealPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${pIdx + 1}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }

            // Pitfalls Section
            AnimatedVisibility(visible = state.isPitfallsRevealed, enter = fadeIn(), exit = fadeOut()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF3E0),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DentalWarningOrange.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "تحذير سريري",
                            tint = DentalWarningOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "فخ سريري شائع يجب الحذر منه (Clinical Pitfall):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB74700)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentCase.pitfalls,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF422100),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // Student Notes if any
            if (currentCase.userNotes.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ملاحظاتي المدونة لهذه الحالة:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentCase.userNotes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }

    // Note input dialog
    if (showNoteDialog && currentCase != null) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("تدوين ملاحظاتك على الحالة #${currentCase.id}") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("ملاحظاتك السريرية، مراجعك أو أسئلتك") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 6
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveCaseNote(currentCase.id, noteText)
                        showNoteDialog = false
                    }
                ) {
                    Text("حفظ الملاحظة")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun BadgeChip(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
