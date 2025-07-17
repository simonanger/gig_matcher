package com.simonanger.gigmatcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simonanger.gigmatcher.model.Band
import com.simonanger.gigmatcher.model.Gig
import com.simonanger.gigmatcher.data.GenreCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGigScreen(bands: List<Band>, onGigCreated: (Gig) -> Unit) {
    var title by remember { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf(listOf<String>()) }
    var selectedCountry by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var promoterName by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }
    var selectedGenreCategory by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var countryExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }
    
    // Extract available genres from bands (deduplicated)
    val availableGenres = remember(bands) {
        bands.flatMap { it.genres }.distinct().sorted()
    }
    
    // Get genre categories
    val genreCategories = remember { GenreCategories.getCategoryNames() }
    
    // Get genres for selected category
    val categoryGenres = remember(selectedGenreCategory, availableGenres) {
        if (selectedGenreCategory.isNotBlank()) {
            GenreCategories.getGenresForCategory(availableGenres, selectedGenreCategory)
                .filter { !selectedGenres.contains(it) }
        } else {
            emptyList()
        }
    }
    
    // Extract available countries from bands (excluding Unknown)
    val availableCountries = remember(bands) {
        bands.map { it.country }.distinct().filter { it != "Unknown" }.sorted()
    }
    
    // Extract available cities from bands, filtered by selected genres and country
    val availableCities = remember(selectedGenres, selectedCountry, bands) {
        val filteredBands = bands.filter { band ->
            val genreMatch = if (selectedGenres.isNotEmpty()) {
                selectedGenres.any { genre -> band.genres.contains(genre) }
            } else true
            
            val countryMatch = if (selectedCountry.isNotBlank()) {
                band.country == selectedCountry
            } else true
            
            genreMatch && countryMatch
        }
        
        // Extract just the city name (first part before comma) and remove duplicates
        filteredBands.map { band ->
            val cityName = band.location.split(",").firstOrNull()?.trim() ?: band.location
            cityName
        }.distinct().filter { it.isNotBlank() }.sorted()
    }
    
    // Clear selected city if it's not available for the selected genres or country
    LaunchedEffect(selectedGenres, selectedCountry) {
        if (selectedCity.isNotBlank() && !availableCities.contains(selectedCity)) {
            selectedCity = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

        // Genres Section
        Text(
            text = "Genres",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        // Selected genres
        if (selectedGenres.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 100.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(selectedGenres) { genre ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = genre,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                selectedGenres = selectedGenres - genre
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove genre")
                        }
                    }
                }
            }
        }
        
        // Genre Category Selection
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = selectedGenreCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Genre Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                genreCategories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedGenreCategory = category
                            categoryExpanded = false
                        }
                    )
                }
            }
        }
        
        // Genre Selection (only shown when category is selected)
        if (selectedGenreCategory.isNotBlank() && categoryGenres.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = genreExpanded,
                onExpandedChange = { genreExpanded = !genreExpanded }
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Add Genre from $selectedGenreCategory") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = genreExpanded,
                    onDismissRequest = { genreExpanded = false }
                ) {
                    categoryGenres.forEach { genre ->
                        DropdownMenuItem(
                            text = { Text(genre) },
                            onClick = {
                                selectedGenres = selectedGenres + genre
                                genreExpanded = false
                            }
                        )
                    }
                }
            }
        } else if (selectedGenreCategory.isNotBlank() && categoryGenres.isEmpty()) {
            Text(
                text = "No more genres available in $selectedGenreCategory category",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Country Dropdown
        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded }
        ) {
            OutlinedTextField(
                value = selectedCountry,
                onValueChange = {},
                readOnly = true,
                label = { Text("Country") },
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
                if (title.isNotBlank() && selectedGenres.isNotEmpty() &&
                    selectedCountry.isNotBlank() && selectedCity.isNotBlank() && promoterName.isNotBlank()) {

                    val matchingBands = bands.filter { band ->
                        val bandCityName = band.location.split(",").firstOrNull()?.trim() ?: band.location
                        selectedGenres.any { genre -> band.genres.contains(genre) } && 
                        bandCityName == selectedCity && 
                        band.country == selectedCountry
                    }

                    val gig = Gig(
                        id = System.currentTimeMillis().toString(),
                        title = title,
                        genres = selectedGenres,
                        city = selectedCity,
                        promoterName = promoterName,
                        matchingBands = matchingBands
                    )
                    onGigCreated(gig)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && selectedGenres.isNotEmpty() &&
                    selectedCountry.isNotBlank() && selectedCity.isNotBlank() && promoterName.isNotBlank()
        ) {
            Text("Create Gig & Find Bands")
        }

        // Preview matching bands
        if (selectedGenres.isNotEmpty() && selectedCountry.isNotBlank() && selectedCity.isNotBlank()) {
            val matchingBands = bands.filter { band ->
                val bandCityName = band.location.split(",").firstOrNull()?.trim() ?: band.location
                selectedGenres.any { genre -> band.genres.contains(genre) } && 
                bandCityName == selectedCity && 
                band.country == selectedCountry
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