package com.simonanger.gigmatcher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlphabeticalIndex(
    availableLetters: List<String>,
    onLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val allLetters = ('A'..'Z').map { it.toString() }
    
    LazyColumn(
        modifier = modifier
            .width(32.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(allLetters) { letter ->
            val isAvailable = availableLetters.contains(letter)
            
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isAvailable) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .clickable(enabled = isAvailable) { 
                        if (isAvailable) {
                            onLetterClick(letter)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontSize = 12.sp,
                    fontWeight = if (isAvailable) FontWeight.Bold else FontWeight.Normal,
                    color = if (isAvailable) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}