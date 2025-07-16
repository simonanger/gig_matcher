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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBandScreen(onBandCreated: (Band) -> Unit) {
    var bandName by remember { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf(listOf<String>()) }
    var selectedCities by remember { mutableStateOf(listOf<String>()) }
    var newGenre by remember { mutableStateOf("") }
    var newCity by remember { mutableStateOf("") }
    var bandcampLink by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    

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
        
        // Add genre row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newGenre,
                onValueChange = { newGenre = it },
                label = { Text("Add Genre") },
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
                Icon(Icons.Default.Add, contentDescription = "Add genre")
            }
        }

        // Cities Section
        Text(
            text = "Cities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        // Selected cities
        if (selectedCities.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 100.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(selectedCities) { city ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = city,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                selectedCities = selectedCities - city
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove city")
                        }
                    }
                }
            }
        }
        
        // Add city row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newCity,
                onValueChange = { newCity = it },
                label = { Text("Add City") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (newCity.isNotBlank() && !selectedCities.contains(newCity)) {
                        selectedCities = selectedCities + newCity
                        newCity = ""
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add city")
            }
        }

        OutlinedTextField(
            value = bandcampLink,
            onValueChange = { bandcampLink = it },
            label = { Text("Bandcamp Link (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio (optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                if (bandName.isNotBlank() && selectedGenres.isNotEmpty() && selectedCities.isNotEmpty()) {
                    val band = Band(
                        id = System.currentTimeMillis().toString(),
                        name = bandName,
                        genres = selectedGenres,
                        cities = selectedCities,
                        bandcampLink = bandcampLink.ifBlank { null },
                        contact = contact.ifBlank { null },
                        bio = bio
                    )
                    onBandCreated(band)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = bandName.isNotBlank() && selectedGenres.isNotEmpty() && selectedCities.isNotEmpty()
        ) {
            Text("Create Band")
        }
    }
}