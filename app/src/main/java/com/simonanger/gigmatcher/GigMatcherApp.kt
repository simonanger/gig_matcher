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
import com.simonanger.gigmatcher.model.Gig
import com.simonanger.gigmatcher.ui.screens.BandDetailScreen
import com.simonanger.gigmatcher.ui.screens.BandsScreen
import com.simonanger.gigmatcher.ui.screens.CreateGigScreen
import com.simonanger.gigmatcher.ui.screens.GigBandsScreen
import com.simonanger.gigmatcher.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GigMatcherApp() {
    val navController = rememberNavController()
    var gigs by remember { mutableStateOf(listOf<Gig>()) }

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
                    icon = { Icon(Icons.Default.Add, contentDescription = "Create Gig") },
                    label = { Text("Create Gig") },
                    selected = false,
                    onClick = { navController.navigate("create_gig") }
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
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(gigs = gigs, navController = navController)
            }
            composable("create_gig") {
                CreateGigScreen(
                    onGigCreated = { gig ->
                        gigs = gigs + gig
                        navController.navigate("gig_bands/${gig.id}")
                    }
                )
            }
            composable("bands") {
                BandsScreen(bands = sampleBands, navController = navController)
            }
            composable("gig_bands/{gigId}") { backStackEntry ->
                val gigId = backStackEntry.arguments?.getString("gigId")
                val gig = gigs.find { it.id == gigId }
                gig?.let { currentGig ->
                    GigBandsScreen(
                        gig = currentGig,
                        onBackClick = { navController.navigate("home") },
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
                val band = sampleBands.find { it.id == bandId }
                band?.let { BandDetailScreen(band = it) }
            }
        }
    }
}