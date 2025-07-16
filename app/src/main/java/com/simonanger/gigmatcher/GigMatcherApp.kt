package com.simonanger.gigmatcher

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simonanger.gigmatcher.data.sampleBands
import com.simonanger.gigmatcher.model.Band
import com.simonanger.gigmatcher.model.Gig
import com.simonanger.gigmatcher.ui.screens.BandDetailScreen
import com.simonanger.gigmatcher.ui.screens.BandsScreen
import com.simonanger.gigmatcher.ui.screens.CreateBandScreen
import com.simonanger.gigmatcher.ui.screens.CreateGigScreen
import com.simonanger.gigmatcher.ui.screens.GigBandsScreen
import com.simonanger.gigmatcher.ui.screens.GigsScreen
import com.simonanger.gigmatcher.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GigMatcherApp() {
    val navController = rememberNavController()
    var gigs by remember { mutableStateOf(listOf<Gig>()) }
    var bands by remember { mutableStateOf(sampleBands) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gig Matcher") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Gigs") },
                    label = { Text("Gigs") },
                    selected = false,
                    onClick = { navController.navigate("gigs") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Bands") },
                    label = { Text("Bands") },
                    selected = false,
                    onClick = { navController.navigate("bands") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "gigs",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("gigs") {
                GigsScreen(gigs = gigs, navController = navController)
            }
            composable("create_gig") {
                CreateGigScreen(
                    bands = bands,
                    onGigCreated = { gig ->
                        gigs = gigs + gig
                        navController.navigate("gig_bands/${gig.id}")
                    }
                )
            }
            composable("bands") {
                BandsScreen(bands = bands, navController = navController)
            }
            composable("home") {
                HomeScreen(gigs = gigs, navController = navController)
            }
            composable("create_band") {
                CreateBandScreen(
                    onBandCreated = { band ->
                        bands = bands + band
                        navController.navigate("bands")
                    }
                )
            }
            composable("gig_bands/{gigId}") { backStackEntry ->
                val gigId = backStackEntry.arguments?.getString("gigId")
                val gig = gigs.find { it.id == gigId }
                gig?.let { currentGig ->
                    GigBandsScreen(
                        gig = currentGig,
                        onBackClick = { navController.navigate("gigs") },
                        onBandSelected = { band ->
                            // Update the gig with the selected band
                            gigs = gigs.map { g ->
                                if (g.id == currentGig.id) {
                                    val updatedSelectedBands = if (g.selectedBands.contains(band)) {
                                        g.selectedBands - band
                                    } else {
                                        g.selectedBands + band
                                    }
                                    g.copy(selectedBands = updatedSelectedBands)
                                } else {
                                    g
                                }
                            }
                        }
                    )
                }
            }
            composable("band_detail/{bandId}") { backStackEntry ->
                val bandId = backStackEntry.arguments?.getString("bandId")
                val band = bands.find { it.id == bandId }
                band?.let { BandDetailScreen(band = it) }
            }
        }
    }
}