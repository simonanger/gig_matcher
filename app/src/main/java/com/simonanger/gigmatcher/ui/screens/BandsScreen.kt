// ===== app/src/main/java/com/example/gigmatcher/ui/screens/BandsScreen.kt =====
package com.simonanger.gigmatcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simonanger.gigmatcher.R
import com.simonanger.gigmatcher.model.Band
import com.simonanger.gigmatcher.ui.components.AlphabeticalIndex
import com.simonanger.gigmatcher.ui.components.BandFilterDialog
import com.simonanger.gigmatcher.ui.components.BandFilters
import com.simonanger.gigmatcher.ui.components.applyBandFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandsScreen(bands: List<Band>, navController: NavController) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var activeFilters by remember { mutableStateOf(BandFilters()) }
    
    val filteredBands = remember(bands, activeFilters) {
        applyBandFilters(bands, activeFilters).sortedBy { it.name.uppercase() }
    }
    
    // Group bands by first letter and create letter-to-index mapping
    val groupedBands = remember(filteredBands) {
        filteredBands.groupBy { it.name.first().uppercase() }
    }
    
    val letterToIndex = remember(groupedBands) {
        val map = mutableMapOf<String, Int>()
        var currentIndex = 0
        groupedBands.keys.sorted().forEach { letter ->
            map[letter] = currentIndex
            currentIndex += groupedBands[letter]?.size ?: 0
        }
        map
    }
    
    val availableLetters = remember(groupedBands) {
        groupedBands.keys.sorted()
    }
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
            // Header with filter button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All Bands",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(
                    onClick = { showFilterDialog = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_list_24px),
                        contentDescription = "Filter bands",
                        tint = if (activeFilters.hasActiveFilters()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            // Active filters summary
            if (activeFilters.hasActiveFilters()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeFilters.getActiveFilterCount()} filter(s) active",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        TextButton(
                            onClick = { activeFilters = BandFilters() }
                        ) {
                            Text("Clear All")
                        }
                    }
                }
            }

            // Band count
            Text(
                text = "Showing ${filteredBands.size} bands",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredBands) { band ->
                    BandCard(band = band, navController = navController)
                }
            }
            }
            
            // Alphabetical index on the right side
            AlphabeticalIndex(
                availableLetters = availableLetters,
                onLetterClick = { letter ->
                    letterToIndex[letter]?.let { index ->
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }
                    }
                },
                modifier = Modifier
                    .padding(
                        top = 16.dp, 
                        bottom = 80.dp, // Add bottom padding to avoid FAB
                        start = 8.dp, 
                        end = 8.dp
                    )
            )
        }
        
        FloatingActionButton(
            onClick = { navController.navigate("create_band") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create Band")
        }
    }
    
    // Filter dialog
    if (showFilterDialog) {
        BandFilterDialog(
            bands = bands,
            currentFilters = activeFilters,
            onFiltersChanged = { newFilters ->
                activeFilters = newFilters
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandCard(band: Band, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate("band_detail/${band.id}") }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = band.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = band.genres.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = band.location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = band.country,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}