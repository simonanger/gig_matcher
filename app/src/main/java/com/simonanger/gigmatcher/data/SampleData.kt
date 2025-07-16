package com.simonanger.gigmatcher.data

import com.simonanger.gigmatcher.model.Band

val sampleBands = listOf(
    Band(
        id = "1",
        name = "Electric Wolves",
        genres = listOf("Rock", "Alternative"),
        cities = listOf("London", "Brighton", "Manchester"),
        bandcampLink = "https://electricwolves.bandcamp.com",
        contact = "electricWolves@gmail.com",
        bio = "High-energy rock band from London with a passion for alternative sounds.",
    ),
    Band(
        id = "2",
        name = "Midnight Echoes",
        genres = listOf("Indie", "Electronic"),
        cities = listOf("Bristol", "Cardiff", "Birmingham"),
        bandcampLink = "https://midnightechoes.bandcamp.com",
        bio = "Electronic indie duo creating atmospheric soundscapes."
    ),
    Band(
        id = "3",
        name = "The Broken Strings",
        genres = listOf("Folk", "Acoustic"),
        cities = listOf("Edinburgh", "Glasgow", "Newcastle"),
        bio = "Traditional folk with a modern twist, storytelling through music."
    ),
    Band(
        id = "4",
        name = "Neon Nights",
        genres = listOf("Electronic", "Synthwave"),
        cities = listOf("London", "Manchester", "Liverpool"),
        bandcampLink = "https://neonnights.bandcamp.com",
        contact = "neonnights666@gmail.com",
        bio = "Synthwave collective bringing 80s vibes to modern dancefloors."
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