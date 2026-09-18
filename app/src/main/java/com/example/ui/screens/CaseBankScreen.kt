package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CaseBankScreen(
    state: DentalUiState,
    viewModel: DentalViewModel,
    modifier: Modifier = Modifier
) {
    // Filter cases based on state
    val filteredCases = remember(
        state.allCases,
        state.bankSearchQuery,
        state.bankSpecialtyFilter,
        state.bankDifficultyFilter,
        state.bankSelectedToothFilter,
        state.bankOnlyFavorites
    ) {
        state.allCases.filter { c ->
            val matchQuery = if (state.bankSearchQuery.isBlank()) true else {
                c.title.contains(state.bankSearchQuery, ignoreCase = true) ||
                c.diagnosis.contains(state.bankSearchQuery, ignoreCase = true) ||
                c.summary.contains(state.bankSearchQuery, ignoreCase = true) ||
                c.tooth == state.bankSearchQuery.trim() ||
                c.specLabel.contains(state.bankSearchQuery, ignoreCase = true)
            }
            val matchSpec = state.bankSpecialtyFilter == "all" || c.spec == state.bankSpecialtyFilter
            val matchDiff = state.bankDifficultyFilter == "all" || c.diff == state.bankDifficultyFilter
            val matchTooth = state.bankSelectedToothFilter == null || c.tooth == state.bankSelectedToothFilter
            val matchFav = !state.bankOnlyFavorites || c.isFavorite

            matchQuery && matchSpec && matchDiff && matchTooth && matchFav
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = state.bankSearchQuery,
            onValueChange = { viewModel.setBankSearchQuery(it) },
            placeholder = { Text("ابحث عن تشخيص، سن FDI، أو أعراض...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "بحث")
            },
            trailingIcon = {
                if (state.bankSearchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setBankSearchQuery("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bank_search_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DentalTealPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Specialty Filters
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = state.bankSpecialtyFilter == "all",
                onClick = { viewModel.setBankSpecialtyFilter("all") },
                label = { Text("الكل", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DentalTealPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = DentalTealPrimary
                )
            )

            DentalSpecialty.entries.filter { it != DentalSpecialty.ALL }.forEach { spec ->
                FilterChip(
                    selected = state.bankSpecialtyFilter == spec.id,
                    onClick = { viewModel.setBankSpecialtyFilter(spec.id) },
                    label = { Text(spec.arabicName, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DentalTealPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = DentalTealPrimary
                    )
                )
            }

            // Favorites only chip
            FilterChip(
                selected = state.bankOnlyFavorites,
                onClick = { viewModel.toggleBankOnlyFavorites() },
                label = { Text("المفضلة ⭐", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DentalWarningOrange.copy(alpha = 0.2f),
                    selectedLabelColor = DentalWarningOrange
                )
            )
        }

        // Active filters & total counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "النتائج المعروضة: ${filteredCases.size} حالة",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (state.bankSelectedToothFilter != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DentalWarningOrange.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { viewModel.setBankToothFilter(null) }
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "السن: #${state.bankSelectedToothFilter} ✕",
                            style = MaterialTheme.typography.labelSmall,
                            color = DentalWarningOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Case Cards List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredCases, key = { it.id }) { item ->
                CaseListItemCard(
                    caseItem = item,
                    onClick = { viewModel.selectDetailCase(item) },
                    onFavoriteClick = { viewModel.toggleFavorite(item) }
                )
            }
        }
    }

    // Modal Bottom Sheet for Case Detail
    state.selectedDetailCase?.let { detailCase ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.selectDetailCase(null) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            CaseDetailBottomSheetContent(
                caseItem = detailCase,
                onClose = { viewModel.selectDetailCase(null) },
                onToggleFavorite = { viewModel.toggleFavorite(detailCase) }
            )
        }
    }
}

@Composable
fun CaseListItemCard(
    caseItem: DentalCase,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(DentalTealPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${caseItem.id}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DentalTealPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = caseItem.specLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = DentalTealPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Tooth Badge
                    BadgeChip(
                        text = "#${caseItem.tooth}",
                        bgColor = DentalWarningOrange.copy(alpha = 0.12f),
                        textColor = DentalWarningOrange
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (caseItem.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "المفضلة",
                            tint = if (caseItem.isFavorite) DentalWarningOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = caseItem.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = caseItem.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "العمر: ${caseItem.patientAge} سنة • ${caseItem.diff}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "عرض التفاصيل ←",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = DentalTealPrimary
                )
            }
        }
    }
}

@Composable
fun CaseDetailBottomSheetContent(
    caseItem: DentalCase,
    onClose: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "الحالة #${caseItem.id} • ${caseItem.specLabel}",
                    style = MaterialTheme.typography.labelMedium,
                    color = DentalTealPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = caseItem.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (caseItem.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "حفظ",
                    tint = if (caseItem.isFavorite) DentalWarningOrange else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tooth & Patient info
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BadgeChip(
                text = "السن: ${caseItem.toothNameArabic}",
                bgColor = DentalWarningOrange.copy(alpha = 0.15f),
                textColor = DentalWarningOrange
            )
            BadgeChip(
                text = "العمر: ${caseItem.patientAge} سنة",
                bgColor = DentalBlueSecondary.copy(alpha = 0.15f),
                textColor = DentalBlueSecondary
            )
            BadgeChip(
                text = "المستوى: ${caseItem.diff}",
                bgColor = DentalSuccessGreen.copy(alpha = 0.15f),
                textColor = DentalSuccessGreen
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Case Summary
        Text(
            text = "القصة السريرية:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = DentalBlueSecondary
        )
        Text(
            text = caseItem.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Diagnosis
        Text(
            text = "التشخيص الطبي الأكيد (Diagnosis):",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = DentalTealPrimary
        )
        Text(
            text = caseItem.diagnosis,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Plan
        Text(
            text = "خطة العلاج (Treatment Plan):",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = DentalSuccessGreen
        )
        caseItem.plan.forEachIndexed { idx, p ->
            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                Text(
                    text = "${idx + 1}. ",
                    fontWeight = FontWeight.Bold,
                    color = DentalSuccessGreen
                )
                Text(
                    text = p,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pitfalls
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFFF3E0),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = DentalWarningOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "فخ سريري:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB74700),
                        fontSize = 12.sp
                    )
                    Text(
                        text = caseItem.pitfalls,
                        fontSize = 13.sp,
                        color = Color(0xFF422100)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
