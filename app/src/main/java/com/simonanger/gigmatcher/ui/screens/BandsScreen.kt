// ===== app/src/main/java/com/example/gigmatcher/ui/screens/BandsScreen.kt =====
package com.simonanger.gigmatcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simonanger.gigmatcher.model.Band

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandsScreen(bands: List<Band>, navController: NavController) {
    var selectedCountry by remember { mutableStateOf("All") }
    var countryExpanded by remember { mutableStateOf(false) }
    
    val availableCountries = remember(bands) {
        listOf("All") + bands.map { it.country }.distinct().filter { it != "Unknown" }.sorted()
    }
    
    val filteredBands = remember(bands, selectedCountry) {
        if (selectedCountry == "All") {
            bands
        } else {
            bands.filter { it.country == selectedCountry }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "All Bands",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Country Filter
            ExposedDropdownMenuBox(
                expanded = countryExpanded,
                onExpandedChange = { countryExpanded = !countryExpanded },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter by Country") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false }
                ) {
                    availableCountries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                selectedCountry = country
                                countryExpanded = false
                            }
                        )
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredBands) { band ->
                    BandCard(band = band, navController = navController)
                }
            }
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