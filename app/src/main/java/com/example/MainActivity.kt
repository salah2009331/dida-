package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppNavDestination
import com.example.ui.DentalViewModel
import com.example.ui.screens.CaseBankScreen
import com.example.ui.screens.CaseSimulatorScreen
import com.example.ui.screens.FdiReferenceScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.DentalTealLight
import com.example.ui.theme.DentalTealPrimary
import com.example.ui.theme.DentalWarningOrange
import com.example.ui.theme.DoctorDidaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DentalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoctorDidaTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    DoctorDidaApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDidaApp(viewModel: DentalViewModel) {
    val state by viewModel.uiState.collectAsState()
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Doctor:Dida",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text(
                                text = "الرفيق السريري لطالب طب الأسنان",
                                fontSize = 11.sp,
                                color = DentalTealLight.copy(alpha = 0.9f)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAboutDialog = true },
                        modifier = Modifier.testTag("about_app_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "حول التطبيق",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DentalTealPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = state.currentDestination == AppNavDestination.SIMULATOR,
                    onClick = { viewModel.navigateTo(AppNavDestination.SIMULATOR) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "المحاكي") },
                    label = { Text("المحاكي", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DentalTealPrimary,
                        selectedTextColor = DentalTealPrimary,
                        indicatorColor = DentalTealPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_simulator")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppNavDestination.BANK,
                    onClick = { viewModel.navigateTo(AppNavDestination.BANK) },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "بنك الحالات") },
                    label = { Text("بنك الحالات", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DentalTealPrimary,
                        selectedTextColor = DentalTealPrimary,
                        indicatorColor = DentalTealPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_bank")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppNavDestination.QUIZ,
                    onClick = { viewModel.navigateTo(AppNavDestination.QUIZ) },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "اختبار") },
                    label = { Text("اختبار سريري", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DentalTealPrimary,
                        selectedTextColor = DentalTealPrimary,
                        indicatorColor = DentalTealPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_quiz")
                )

                NavigationBarItem(
                    selected = state.currentDestination == AppNavDestination.FDI_GUIDE,
                    onClick = { viewModel.navigateTo(AppNavDestination.FDI_GUIDE) },
                    icon = { Icon(Icons.Default.ViewModule, contentDescription = "دليل FDI") },
                    label = { Text("دليل FDI", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DentalTealPrimary,
                        selectedTextColor = DentalTealPrimary,
                        indicatorColor = DentalTealPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_fdi")
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = DentalTealPrimary)
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = "جاري تحميل 1000 حالة سريرية...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Crossfade(
                targetState = state.currentDestination,
                modifier = Modifier.padding(innerPadding),
                label = "screen_crossfade"
            ) { dest ->
                when (dest) {
                    AppNavDestination.SIMULATOR -> CaseSimulatorScreen(state = state, viewModel = viewModel)
                    AppNavDestination.BANK -> CaseBankScreen(state = state, viewModel = viewModel)
                    AppNavDestination.QUIZ -> QuizScreen(state = state, viewModel = viewModel)
                    AppNavDestination.FDI_GUIDE -> FdiReferenceScreen(state = state, viewModel = viewModel)
                }
            }
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = DentalTealPrimary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "برنامج Doctor:Dida لطلاب طب الأسنان",
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            },
            text = {
                Column {
                    Text(
                        text = "فكرة البرنامج:\n" +
                                "تطبيق سريري ذكي موجه لطالب كلية طب الأسنان يدمج بين التدريب العملي والتفكير السريري المنهجي:\n\n" +
                                "1. 💡 توليد الحالات السريرية: محاكاة فورية لحالات واقعية من بين 1,000 حالة متنوعة.\n" +
                                "2. 🦷 نظام ترقيم الأسنان FDI: تفاعل مرئي مع مخطط الفك العلوي والسفلي لتحديد السن المصاب.\n" +
                                "3. 🎯 تحدي التشخيص وخطة العلاج: اختبار سريري تفاعلي مع خطوات العلاج الصحيحة.\n" +
                                "4. ⚠️ فخاخ وأخطاء سريرية شائعة: تنبيه الطالب للأخطاء الشائعة في العيادة لتفاديها.\n" +
                                "5. 🏆 بنك الحالات والبحث والمفضلة: إمكانية حفظ الحالات وتدوين الملاحظات الشخصية.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("حسناً", fontWeight = FontWeight.Bold, color = DentalTealPrimary)
                }
            }
        )
    }
}
