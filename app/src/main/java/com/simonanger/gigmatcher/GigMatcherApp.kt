package com.simonanger.gigmatcher

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simonanger.gigmatcher.data.loadBandsFromCsv
import com.simonanger.gigmatcher.data.sampleBands
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
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        try {
            val inputStream = context.assets.open("uk_active_bands_standardized.csv")
            val csvBands = loadBandsFromCsv(inputStream)
            bands = csvBands
        } catch (e: Exception) {
            // Fall back to sample bands if CSV loading fails
            bands = sampleBands
        }
    }

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
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.metal_horns),
                            contentDescription = "Gigs",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Gigs") },
                    selected = false,
                    onClick = { navController.navigate("gigs") }
                )
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.bandicon),
                            contentDescription = "Bands",
                            modifier = Modifier.size(29.dp)
                        )
                    },
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
                GigsScreen(
                    gigs = gigs, 
                    navController = navController,
                    onEditGig = { gig ->
                        // Navigate to edit screen - we'll need to create this
                        navController.navigate("edit_gig/${gig.id}")
                    },
                    onDeleteGig = { gig ->
                        // Remove the gig from the list
                        gigs = gigs.filter { it.id != gig.id }
                    }
                )
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
            composable("edit_gig/{gigId}") { backStackEntry ->
                val gigId = backStackEntry.arguments?.getString("gigId")
                val gig = gigs.find { it.id == gigId }
                gig?.let { currentGig ->
                    CreateGigScreen(
                        bands = bands,
                        existingGig = currentGig,
                        onGigCreated = { updatedGig ->
                            gigs = gigs.map { g ->
                                if (g.id == currentGig.id) updatedGig else g
                            }
                            // Navigate back to band selection after editing
                            navController.navigate("gig_bands/${updatedGig.id}")
                        },
                        onBackClick = { 
                            // Check if we came from band selection
                            val previousRoute = navController.previousBackStackEntry?.destination?.route
                            if (previousRoute?.startsWith("gig_bands/") == true) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("gigs")
                            }
                        }
                    )
                }
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
                        navController = navController,
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
                        },
                        onEditGig = { gig ->
                            navController.navigate("edit_gig/${gig.id}")
                        }
                    )
                }
            }
            composable("band_detail/{bandId}?from={from}&gigId={gigId}") { backStackEntry ->
                val bandId = backStackEntry.arguments?.getString("bandId")
                val from = backStackEntry.arguments?.getString("from")
                val gigId = backStackEntry.arguments?.getString("gigId")
                val band = bands.find { it.id == bandId }
                band?.let { 
                    val onBackClick = if (from == "gig_bands" && gigId != null) {
                        { navController.navigate("gig_bands/$gigId") }
                    } else null
                    BandDetailScreen(band = it, onBackClick = onBackClick) 
                }
            }
        }
    }
}