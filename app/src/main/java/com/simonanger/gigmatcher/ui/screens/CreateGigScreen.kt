package com.simonanger.gigmatcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simonanger.gigmatcher.model.Band
import com.simonanger.gigmatcher.model.Gig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGigScreen(bands: List<Band>, onGigCreated: (Gig) -> Unit) {
    var title by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var promoterName by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }
    
    // Extract available genres from bands
    val availableGenres = remember(bands) {
        bands.flatMap { it.genres }.distinct().sorted()
    }
    
    // Extract available cities from bands, filtered by selected genre
    val availableCities = remember(selectedGenre, bands) {
        if (selectedGenre.isNotBlank()) {
            bands
                .filter { it.genres.contains(selectedGenre) }
                .flatMap { it.cities }
                .distinct()
                .sorted()
        } else {
            bands.flatMap { it.cities }.distinct().sorted()
        }
    }
    
    // Clear selected city if it's not available for the selected genre
    LaunchedEffect(selectedGenre) {
        if (selectedGenre.isNotBlank() && selectedCity.isNotBlank() && !availableCities.contains(selectedCity)) {
            selectedCity = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create New Gig",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Gig Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = promoterName,
            onValueChange = { promoterName = it },
            label = { Text("Promoter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Genre Dropdown
        ExposedDropdownMenuBox(
            expanded = genreExpanded,
            onExpandedChange = { genreExpanded = !genreExpanded }
        ) {
            OutlinedTextField(
                value = selectedGenre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Genre") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = genreExpanded,
                onDismissRequest = { genreExpanded = false }
            ) {
                availableGenres.forEach { genre ->
                    DropdownMenuItem(
                        text = { Text(genre) },
                        onClick = {
                            selectedGenre = genre
                            genreExpanded = false
                        }
                    )
                }
            }
        }

        // City Dropdown
        ExposedDropdownMenuBox(
            expanded = cityExpanded,
            onExpandedChange = { cityExpanded = !cityExpanded }
        ) {
            OutlinedTextField(
                value = selectedCity,
                onValueChange = {},
                readOnly = true,
                label = { Text("City") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = cityExpanded,
                onDismissRequest = { cityExpanded = false }
            ) {
                availableCities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city) },
                        onClick = {
                            selectedCity = city
                            cityExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                if (title.isNotBlank() && selectedGenre.isNotBlank() &&
                    selectedCity.isNotBlank() && promoterName.isNotBlank()) {

                    val matchingBands = bands.filter { band ->
                        band.genres.contains(selectedGenre) && band.cities.contains(selectedCity)
                    }

                    val gig = Gig(
                        id = System.currentTimeMillis().toString(),
                        title = title,
                        genre = selectedGenre,
                        city = selectedCity,
                        promoterName = promoterName,
                        matchingBands = matchingBands
                    )
                    onGigCreated(gig)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && selectedGenre.isNotBlank() &&
                    selectedCity.isNotBlank() && promoterName.isNotBlank()
        ) {
            Text("Create Gig & Find Bands")
        }

        // Preview matching bands
        if (selectedGenre.isNotBlank() && selectedCity.isNotBlank()) {
            val matchingBands = bands.filter { band ->
                band.genres.contains(selectedGenre) && band.cities.contains(selectedCity)
            }

            if (matchingBands.isNotEmpty()) {
                Text(
                    text = "Potential matches: ${matchingBands.size} bands",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}