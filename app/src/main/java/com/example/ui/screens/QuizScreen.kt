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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DentalUiState
import com.example.ui.DentalViewModel
import com.example.ui.components.FdiToothChart
import com.example.ui.theme.DentalBlueSecondary
import com.example.ui.theme.DentalSuccessGreen
import com.example.ui.theme.DentalTealPrimary
import com.example.ui.theme.DentalWarningOrange

@Composable
fun QuizScreen(
    state: DentalUiState,
    viewModel: DentalViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!state.isQuizActive) {
            // Welcome to Quiz Screen
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(DentalTealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = DentalTealPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "التدريب السريري السريع",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "اختبر مهاراتك التشخيصية في طب الأسنان من خلال 5 حالات سريرية واقعية مختارة عشوائياً من بنك الحالات.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.startQuiz(5) },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .testTag("start_quiz_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DentalTealPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("بدء الاختبار الآن (5 حالات)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (state.isQuizFinished) {
            // Quiz Result Screen
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(DentalWarningOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = DentalWarningOrange,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "نتيجة التدريب السريري",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${state.quizScore} من أصل ${state.quizQuestions.size}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DentalTealPrimary
                    )

                    val percentage = (state.quizScore.toFloat() / state.quizQuestions.size * 100).toInt()
                    val appraisal = when {
                        percentage >= 80 -> "طبيب أسنان متميز! تشخيصاتك دقيقة وواعدة 🌟"
                        percentage >= 60 -> "أداء جيد جداً! مع مزيد من التدريب ستصل لقمة الدقة السريرية 👍"
                        else -> "بداية طيبة، راجع الحالات في بنك الحالات وتعرف على الفخاخ السريرية 📚"
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = appraisal,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.startQuiz(5) },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(containerColor = DentalTealPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("إعادة الاختبار مع حالات جديدة", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Active Quiz Question
            val currentQ = state.quizQuestions.getOrNull(state.currentQuizIndex)
            if (currentQ != null) {
                // Progress indicator
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الحالة السريرية ${state.currentQuizIndex + 1} من ${state.quizQuestions.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DentalTealPrimary
                        )
                        Text(
                            text = "النقاط: ${state.quizScore}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DentalWarningOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (state.currentQuizIndex + 1).toFloat() / state.quizQuestions.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = DentalTealPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                // Question Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BadgeChip(
                                text = currentQ.caseItem.specLabel,
                                bgColor = DentalTealPrimary.copy(alpha = 0.15f),
                                textColor = DentalTealPrimary
                            )
                            BadgeChip(
                                text = "السن FDI: #${currentQ.caseItem.tooth}",
                                bgColor = DentalWarningOrange.copy(alpha = 0.15f),
                                textColor = DentalWarningOrange
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "بيانات الحالة السريرية:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DentalBlueSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQ.caseItem.summary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "العمر: ${currentQ.caseItem.patientAge} سنة • الموضع: ${currentQ.caseItem.toothNameArabic}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Options Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ما هو التشخيص الأدق لهذه الحالة؟",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        currentQ.options.forEachIndexed { optIndex, optionText ->
                            val isChosen = currentQ.selectedOptionIndex == optIndex
                            val isCorrect = optIndex == currentQ.correctIndex
                            val isAnswered = currentQ.isSubmitted

                            val borderColor = when {
                                isAnswered && isCorrect -> DentalSuccessGreen
                                isAnswered && isChosen && !isCorrect -> Color(0xFFD32F2F)
                                isChosen -> DentalTealPrimary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            }

                            val bgColor = when {
                                isAnswered && isCorrect -> DentalSuccessGreen.copy(alpha = 0.15f)
                                isAnswered && isChosen && !isCorrect -> Color(0xFFD32F2F).copy(alpha = 0.12f)
                                isChosen -> DentalTealPrimary.copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                    .clickable(enabled = !isAnswered) {
                                        viewModel.answerQuizQuestion(optIndex)
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
                                                text = ('1'.code + optIndex).toChar().toString(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = borderColor
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isChosen || (isAnswered && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    if (isAnswered && isCorrect) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "صحيح",
                                            tint = DentalSuccessGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else if (isAnswered && isChosen && !isCorrect) {
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

                        // Next button once submitted
                        if (currentQ.isSubmitted) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { viewModel.nextQuizQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_next_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = DentalTealPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (state.currentQuizIndex + 1 < state.quizQuestions.size) "الحالة التالية ←" else "عرض النتيجة النهائية 🏆",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
