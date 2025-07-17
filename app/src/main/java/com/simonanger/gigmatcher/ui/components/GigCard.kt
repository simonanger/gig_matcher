package com.simonanger.gigmatcher.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simonanger.gigmatcher.model.Gig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GigCard(gig: Gig, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate("gig_bands/${gig.id}") }
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
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Promoter: ${gig.promoterName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Matching Bands: ${gig.matchingBands.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Selected: ${gig.selectedBands.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}