package com.simonanger.gigmatcher.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.simonanger.gigmatcher.data.GenreCategories
import com.simonanger.gigmatcher.model.Band

data class BandFilters(
    val selectedCountries: Set<String> = emptySet(),
    val selectedCities: Set<String> = emptySet(),
    val selectedGenres: Set<String> = emptySet(),
    val selectedGenreCategories: Set<String> = emptySet(),
    val selectedStatuses: Set<String> = emptySet()
) {
    fun hasActiveFilters(): Boolean {
        return selectedCountries.isNotEmpty() || 
               selectedCities.isNotEmpty() || 
               selectedGenres.isNotEmpty() || 
               selectedGenreCategories.isNotEmpty() ||
               selectedStatuses.isNotEmpty()
    }
    
    fun getActiveFilterCount(): Int {
        return selectedCountries.size + 
               selectedCities.size + 
               selectedGenres.size + 
               selectedGenreCategories.size +
               selectedStatuses.size
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandFilterDialog(
    bands: List<Band>,
    currentFilters: BandFilters,
    onFiltersChanged: (BandFilters) -> Unit,
    onDismiss: () -> Unit
) {
    var filters by remember { mutableStateOf(currentFilters) }
    
    // Extract unique values from bands
    val availableCountries = remember(bands) {
        bands.map { it.country }.distinct().filter { it != "Unknown" }.sorted()
    }
    
    val availableCities = remember(bands, filters.selectedCountries) {
        val relevantBands = if (filters.selectedCountries.isEmpty()) {
            bands
        } else {
            bands.filter { filters.selectedCountries.contains(it.country) }
        }
        relevantBands.map { it.location.split(",").first().trim() }.distinct().sorted()
    }
    
    val availableGenres = remember(bands, filters.selectedGenreCategories) {
        val allGenres = bands.flatMap { it.genres }.distinct()
        
        if (filters.selectedGenreCategories.isEmpty()) {
            allGenres.sorted()
        } else {
            // Filter genres to only show those in selected categories
            val filteredGenres = mutableListOf<String>()
            filters.selectedGenreCategories.forEach { categoryName ->
                val categoryGenres = GenreCategories.getGenresForCategory(allGenres, categoryName)
                filteredGenres.addAll(categoryGenres)
            }
            filteredGenres.distinct().sorted()
        }
    }
    
    val availableStatuses = remember(bands) {
        bands.map { it.status }.distinct().sorted()
    }
    
    val genreCategories = remember {
        GenreCategories.getCategoryNames()
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter Bands",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Clear, contentDescription = "Close")
                    }
                }
                
                Divider()
                
                // Filter content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    
                    // Countries
                    item {
                        FilterSection(
                            title = "Countries",
                            items = availableCountries,
                            selectedItems = filters.selectedCountries,
                            onSelectionChanged = { countries ->
                                // Clear city selections when countries change
                                filters = filters.copy(
                                    selectedCountries = countries,
                                    selectedCities = emptySet()
                                )
                            }
                        )
                    }
                    
                    // Cities
                    item {
                        FilterSection(
                            title = "Cities",
                            items = availableCities,
                            selectedItems = filters.selectedCities,
                            onSelectionChanged = { cities ->
                                filters = filters.copy(selectedCities = cities)
                            }
                        )
                    }
                    
                    // Genre Categories
                    item {
                        FilterSection(
                            title = "Genre Categories",
                            items = genreCategories,
                            selectedItems = filters.selectedGenreCategories,
                            onSelectionChanged = { categories ->
                                // Clear specific genre selections when categories change
                                filters = filters.copy(
                                    selectedGenreCategories = categories,
                                    selectedGenres = emptySet()
                                )
                            }
                        )
                    }
                    
                    // Individual Genres
                    item {
                        FilterSection(
                            title = "Specific Genres",
                            items = availableGenres,
                            selectedItems = filters.selectedGenres,
                            onSelectionChanged = { genres ->
                                filters = filters.copy(selectedGenres = genres)
                            }
                        )
                    }
                    
                    // Status
                    item {
                        FilterSection(
                            title = "Status",
                            items = availableStatuses,
                            selectedItems = filters.selectedStatuses,
                            onSelectionChanged = { statuses ->
                                filters = filters.copy(selectedStatuses = statuses)
                            }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
                
                Divider()
                
                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            filters = BandFilters()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear All")
                    }
                    
                    Button(
                        onClick = {
                            onFiltersChanged(filters)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply Filters")
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    items: List<String>,
    selectedItems: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        if (items.isEmpty()) {
            Text(
                text = "No options available",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(
                modifier = Modifier
                    .heightIn(max = 200.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = selectedItems.contains(item),
                                role = Role.Checkbox,
                                onValueChange = { isSelected ->
                                    val newSelection = if (isSelected) {
                                        selectedItems + item
                                    } else {
                                        selectedItems - item
                                    }
                                    onSelectionChanged(newSelection)
                                }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedItems.contains(item),
                            onCheckedChange = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

fun applyBandFilters(bands: List<Band>, filters: BandFilters): List<Band> {
    return bands.filter { band ->
        // Country filter
        val countryMatch = filters.selectedCountries.isEmpty() || 
                          filters.selectedCountries.contains(band.country)
        
        // City filter
        val cityMatch = filters.selectedCities.isEmpty() || 
                       filters.selectedCities.any { city ->
                           band.location.contains(city, ignoreCase = true)
                       }
        
        // Individual genre filter
        val genreMatch = filters.selectedGenres.isEmpty() || 
                        band.genres.any { genre ->
                            filters.selectedGenres.contains(genre)
                        }
        
        // Genre category filter
        val categoryMatch = filters.selectedGenreCategories.isEmpty() || 
                           band.genres.any { genre ->
                               val category = GenreCategories.categorizeGenre(genre)
                               filters.selectedGenreCategories.contains(category)
                           }
        
        // Status filter
        val statusMatch = filters.selectedStatuses.isEmpty() || 
                         filters.selectedStatuses.contains(band.status)
        
        countryMatch && cityMatch && genreMatch && categoryMatch && statusMatch
    }
}