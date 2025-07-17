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
import com.simonanger.gigmatcher.data.GenreCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBandScreen(onBandCreated: (Band) -> Unit) {
    var bandName by remember { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf(listOf<String>()) }
    var newGenre by remember { mutableStateOf("") }
    var selectedGenreCategory by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("England") }
    var url by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Active") }
    var countryExpanded by remember { mutableStateOf(false) }
    
    val availableCountries = listOf("England", "Scotland", "Wales", "Northern Ireland", "Ireland")
    
    // Get genre categories and common genres for the selected category
    val genreCategories = remember { GenreCategories.getCategoryNames() }
    val categoryGenres = remember(selectedGenreCategory) {
        if (selectedGenreCategory.isNotBlank()) {
            val category = GenreCategories.categories.find { it.name == selectedGenreCategory }
            category?.keywords?.filter { !selectedGenres.contains(it) } ?: emptyList()
        } else {
            emptyList()
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
            text = "Create New Band",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = bandName,
            onValueChange = { bandName = it },
            label = { Text("Band Name") },
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
        
        // Quick genre selection from category
        if (selectedGenreCategory.isNotBlank() && categoryGenres.isNotEmpty()) {
            Text(
                text = "Common $selectedGenreCategory genres:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            // Show genre buttons in a flow layout
            categoryGenres.take(6).chunked(2).forEach { genreRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genreRow.forEach { genre ->
                        Button(
                            onClick = {
                                selectedGenres = selectedGenres + genre
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                    }
                    // Fill remaining space if odd number of genres
                    if (genreRow.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        // Manual genre input (fallback)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newGenre,
                onValueChange = { newGenre = it },
                label = { Text("Custom Genre") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (newGenre.isNotBlank() && !selectedGenres.contains(newGenre)) {
                        selectedGenres = selectedGenres + newGenre
                        newGenre = ""
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add custom genre")
            }
        }

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        // Country Dropdown
        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded }
        ) {
            OutlinedTextField(
                value = country,
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
                availableCountries.forEach { availableCountry ->
                    DropdownMenuItem(
                        text = { Text(availableCountry) },
                        onClick = {
                            country = availableCountry
                            countryExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Band URL (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (bandName.isNotBlank() && selectedGenres.isNotEmpty() && location.isNotBlank()) {
                    val band = Band(
                        id = System.currentTimeMillis().toString(),
                        name = bandName,
                        url = url.ifBlank { "" },
                        genres = selectedGenres,
                        location = location,
                        country = country,
                        status = status
                    )
                    onBandCreated(band)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = bandName.isNotBlank() && selectedGenres.isNotEmpty() && location.isNotBlank()
        ) {
            Text("Create Band")
        }
    }
}