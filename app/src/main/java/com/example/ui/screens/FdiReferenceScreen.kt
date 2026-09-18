package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppNavDestination
import com.example.ui.DentalUiState
import com.example.ui.DentalViewModel
import com.example.ui.components.FdiToothChart
import com.example.ui.theme.DentalBlueSecondary
import com.example.ui.theme.DentalTealPrimary
import com.example.ui.theme.DentalWarningOrange
import com.example.ui.theme.Quad1Color
import com.example.ui.theme.Quad2Color
import com.example.ui.theme.Quad3Color
import com.example.ui.theme.Quad4Color

@Composable
fun FdiReferenceScreen(
    state: DentalUiState,
    viewModel: DentalViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val selectedTooth = state.selectedFdiToothInfo ?: "11"

    val quadrant = selectedTooth.firstOrNull()?.digitToIntOrNull() ?: 1
    val toothPos = selectedTooth.lastOrNull()?.digitToIntOrNull() ?: 1

    val quadrantNameAr = when (quadrant) {
        1 -> "الربع الأول: العلوي الأيمن (Upper Right)"
        2 -> "الربع الثاني: العلوي الأيسر (Upper Left)"
        3 -> "الربع الثالث: السفلي الأيسر (Lower Left)"
        4 -> "الربع الرابع: السفلي الأيمن (Lower Right)"
        else -> ""
    }

    val toothNameAr = when (toothPos) {
        1 -> "القاطع المركزي (Central Incisor)"
        2 -> "القاطع الجانبي (Lateral Incisor)"
        3 -> "الناب (Canine)"
        4 -> "الضاحك الأول (1st Premolar)"
        5 -> "الضاحك الثاني (2nd Premolar)"
        6 -> "الرحى الأولى (1st Molar)"
        7 -> "الرحى الثانية (2nd Molar)"
        8 -> "الرحى الثالثة / ضرس العقل (Wisdom Tooth)"
        else -> "السن رقم $toothPos"
    }

    val clinicalTip = when (toothPos) {
        1, 2 -> "الأسنان الأمامية: التركيز على الحفاظ على الناحية التجميلية وتجنب إزالة المينا السليمة، اختبار الحيوية عند الصدمات الرضية."
        3 -> "الناب: سن ذو جذر طويل واستراتيجي في توجيه الإطباق (Canine Guidance). المحافظة عليه أساسية لاستقرار الفك."
        4, 5 -> "الضواحك: الضاحك الأول العلوي غالباً يحتوي على جذرين وقناتين عصبيتين، الحذر أثناء تشكيل القنوات."
        6, 7 -> "الأرحاء (الأضراس): قوة المضغ الأساسية، الأرحاء العلوية غالباً بـ 3 جذور (MB1, MB2, DB, Palatal)، والأرحاء السفلية بقناتين أو 3 قنوات."
        8 -> "ضروس العقل: التقييم الشعاعي للارتباط مع العصب السنخي السفلي والجيوب الفكية قبل اتخاذ قرار الخلع الجراحي."
        else -> "تقييم سريري وشعاعي شامل."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DentalTealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = DentalTealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "نظام ترقيم الأسنان FDI العالمي",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "اضغط على أي سن لعرض تفاصيله ومعلوماته السريرية",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Chart
                FdiToothChart(
                    selectedTooth = selectedTooth,
                    onToothSelected = { viewModel.selectFdiToothForInfo(it) },
                    compact = false
                )
            }
        }

        // Details of selected tooth
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DentalWarningOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = selectedTooth,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = toothNameAr,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = quadrantNameAr,
                                style = MaterialTheme.typography.bodySmall,
                                color = DentalBlueSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ملاحظة سريرية للسن #$selectedTooth:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DentalTealPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = clinicalTip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        viewModel.setBankToothFilter(selectedTooth)
                        viewModel.navigateTo(AppNavDestination.BANK)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DentalTealPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.MedicalServices, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("استعراض حالات السن #$selectedTooth في بنك الحالات", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Educational FDI Guide summary card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "كيف يعمل نظام FDI (World Dental Federation)؟",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "يتكون كل سن من رقمين:\n" +
                            "• الرقم الأول (العشرات): يشير إلى الربع الفكي (1: علوي أيمن، 2: علوي أيسر، 3: سفلي أيسر، 4: سفلي أيمن).\n" +
                            "• الرقم الثاني (الآحاد): يشير لرقم السن من خط المنتصف (1: قاطع مركزي، إلى 8: ضرس العقل).\n" +
                            "• في الأسنان اللبنية: تأخذ الأرباع الأرقام 5، 6، 7، 8 مع الأرقام 1 إلى 5.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
