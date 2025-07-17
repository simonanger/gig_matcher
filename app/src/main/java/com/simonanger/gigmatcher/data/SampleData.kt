package com.simonanger.gigmatcher.data

import com.simonanger.gigmatcher.model.Band
import java.io.BufferedReader
import java.io.InputStreamReader

fun loadBandsFromCsv(inputStream: java.io.InputStream): List<Band> {
    val bands = mutableListOf<Band>()
    val reader = BufferedReader(InputStreamReader(inputStream))
    
    reader.use { br ->
        br.readLine() // Skip header
        var line: String?
        var id = 1
        while (br.readLine().also { line = it } != null) {
            line?.let { csvLine ->
                val parts = parseCsvLine(csvLine)
                if (parts.size >= 4) {
                    val location = parts[3]
                    val country = extractCountry(location)
                    val genreString = parts[2]
                    val genres = parseGenres(genreString)
                    val band = Band(
                        id = id.toString(),
                        name = parts[0],
                        url = parts[1],
                        genres = genres,
                        location = location,
                        country = country,
                        status = if (parts.size > 4) parts[4] else "Active"
                    )
                    bands.add(band)
                    id++
                }
            }
        }
    }
    
    return bands
}

private fun parseGenres(genreString: String): List<String> {
    // Simple split by '/' since the CSV is pre-cleaned
    return genreString
        .split("/")
        .map { it.trim() }
        .filter { it.isNotBlank() }
}

private fun extractCountry(location: String): String {
    return when {
        location.contains("England", ignoreCase = true) -> "England"
        location.contains("Scotland", ignoreCase = true) -> "Scotland"
        location.contains("Wales", ignoreCase = true) -> "Wales"
        location.contains("Northern Ireland", ignoreCase = true) -> "Northern Ireland"
        location.contains("Ireland", ignoreCase = true) && !location.contains("Northern Ireland", ignoreCase = true) -> "Ireland"
        location.isBlank() -> "Unknown"
        else -> "Unknown"
    }
}

private fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var inQuotes = false
    var i = 0
    
    while (i < line.length) {
        val char = line[i]
        when {
            char == '"' && !inQuotes -> {
                inQuotes = true
            }
            char == '"' && inQuotes -> {
                if (i + 1 < line.length && line[i + 1] == '"') {
                    current.append('"')
                    i++
                } else {
                    inQuotes = false
                }
            }
            char == ',' && !inQuotes -> {
                result.add(current.toString())
                current = StringBuilder()
            }
            else -> {
                current.append(char)
            }
        }
        i++
    }
    
    result.add(current.toString())
    return result
}

val sampleBands = listOf(
    Band(
        id = "1",
        name = "Electric Wolves",
        url = "https://electricwolves.bandcamp.com",
        genres = listOf("Rock", "Alternative"),
        location = "London, England",
        country = "England"
    ),
    Band(
        id = "2",
        name = "Midnight Echoes",
        url = "https://midnightechoes.bandcamp.com",
        genres = listOf("Electronic", "Indie"),
        location = "Bristol, England",
        country = "England"
    ),
    Band(
        id = "3",
        name = "The Broken Strings",
        url = "https://thebrokenstrings.bandcamp.com",
        genres = listOf("Folk", "Acoustic"),
        location = "Edinburgh, Scotland",
        country = "Scotland"
    ),
    Band(
        id = "4",
        name = "Neon Nights",
        url = "https://neonnights.bandcamp.com",
        genres = listOf("Synthwave", "Electronic"),
        location = "Manchester, England",
        country = "England"
    )
)

val ukCities = listOf(
    "London", "Manchester", "Birmingham", "Liverpool", "Bristol", "Brighton",
    "Edinburgh", "Glasgow", "Cardiff", "Newcastle", "Nottingham", "Sheffield",
    "Leeds", "York", "Cambridge", "Oxford"
)

val musicGenres = listOf(
    "Rock", "Alternative", "Indie", "Electronic", "Folk", "Acoustic",
    "Metal", "Punk", "Jazz", "Blues", "Reggae", "Hip-Hop", "Pop",
    "Synthwave", "Drum & Bass", "Techno", "House"
)