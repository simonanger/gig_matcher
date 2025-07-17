package com.simonanger.gigmatcher.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simonanger.gigmatcher.model.Band
import com.simonanger.gigmatcher.model.Gig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GigBandsScreen(
    gig: Gig,
    navController: NavController,
    onBackClick: () -> Unit,
    onBandSelected: (Band) -> Unit
) {
    var selectedBands by remember { mutableStateOf(gig.selectedBands.toSet()) }
    // Show matching bands by default if no bands are selected yet
    var showMatchingBands by remember { mutableStateOf(gig.selectedBands.isEmpty()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Bands for ${gig.title}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Gig info header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = gig.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${gig.genres.joinToString(", ")} • ${gig.city}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Promoter: ${gig.promoterName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Calculate available bands once for the entire component
            val availableBands = gig.matchingBands.filter { !selectedBands.contains(it) }

            // Selected bands section
            if (selectedBands.isNotEmpty()) {
                Text(
                    text = "Selected Band${if (selectedBands.size == 1) "" else "s"} (${selectedBands.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = if (availableBands.isEmpty()) {
                        Modifier.fillMaxHeight()
                    } else {
                        Modifier.heightIn(max = 200.dp)
                    },
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedBands.toList()) { band ->
                        SelectedBandCard(
                            band = band,
                            gig = gig,
                            navController = navController,
                            onRemoveClick = {
                                selectedBands = selectedBands - band
                                onBandSelected(band)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Show button to view more matching bands if there are available bands and they're not currently shown
            if (availableBands.isNotEmpty() && !showMatchingBands) {
                Button(
                    onClick = { showMatchingBands = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Find more bands"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find More Bands (${availableBands.size} available)")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Matching bands section - only show when toggled on
            if (showMatchingBands) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Matching Bands (${availableBands.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { showMatchingBands = false }
                    ) {
                        Text("Hide")
                    }
                }

                if (availableBands.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (gig.matchingBands.isEmpty()) {
                                    "No matching bands found for this genre and city combination"
                                } else {
                                    "All matching bands have been selected"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableBands) { band ->
                            BandCard(
                                band = band,
                                gig = gig,
                                isSelected = selectedBands.contains(band),
                                navController = navController,
                                onBandClick = { 
                                    selectedBands = if (selectedBands.contains(band)) {
                                        selectedBands - band
                                    } else {
                                        selectedBands + band
                                    }
                                    onBandSelected(band)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BandCard(
    band: Band,
    gig: Gig,
    isSelected: Boolean,
    navController: NavController,
    onBandClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("band_detail/${band.id}?from=gig_bands&gigId=${gig.id}") }
                    .padding(8.dp)
            ) {
                Text(
                    text = band.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = band.genres.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            
            Button(
                onClick = onBandClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) 
                        MaterialTheme.colorScheme.secondary 
                    else 
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = if (isSelected) "Remove from gig" else "Add to gig"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isSelected) "Remove" else "Add")
            }
        }
    }
}

@Composable
fun SelectedBandCard(
    band: Band,
    gig: Gig,
    navController: NavController,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("band_detail/${band.id}?from=gig_bands&gigId=${gig.id}") }
                    .padding(8.dp)
            ) {
                Text(
                    text = band.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = band.genres.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = band.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = band.country,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            
            IconButton(
                onClick = onRemoveClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove from gig"
                )
            }
        }
    }
}