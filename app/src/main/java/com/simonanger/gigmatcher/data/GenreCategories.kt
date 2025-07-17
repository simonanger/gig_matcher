package com.simonanger.gigmatcher.data

data class GenreCategory(
    val name: String,
    val keywords: List<String>
)

object GenreCategories {
    
    val categories = listOf(
        GenreCategory(
            name = "Metal",
            keywords = listOf(
                "Heavy Metal", "Death Metal", "Black Metal", "Doom Metal", "Thrash Metal",
                "Power Metal", "Progressive Metal", "Symphonic Metal", "Folk Metal",
                "Viking Metal", "Pagan Metal", "Melodic Death Metal", "Technical Death Metal",
                "Brutal Death Metal", "Blackened Death Metal", "Atmospheric Black Metal",
                "Depressive Black Metal", "Raw Black Metal", "Symphonic Black Metal",
                "Progressive Black Metal", "Melodic Black Metal", "Post-Black Metal",
                "Atmospheric Death Metal", "Technical Thrash Metal", "Crossover Thrash",
                "Blackened Thrash Metal", "Speed Metal", "NWOBHM", "Traditional Heavy Metal",
                "Epic Metal", "Gothic Metal", "Industrial Metal", "Nu Metal",
                "Alternative Metal", "Groove Metal", "Southern Metal", "Stoner Metal",
                "Sludge Metal", "Post-Metal", "Atmospheric Metal", "Experimental Metal",
                "Avant-garde Metal", "Dark Metal", "Extreme Metal", "Underground Metal"
            )
        ),
        GenreCategory(
            name = "Rock",
            keywords = listOf(
                "Hard Rock", "Alternative Rock", "Progressive Rock", "Psychedelic Rock",
                "Post-Rock", "Indie Rock", "Classic Rock", "Blues Rock", "Southern Rock",
                "Garage Rock", "Punk Rock", "Post-Punk", "New Wave", "Shoegaze",
                "Britpop", "Grunge", "Noise Rock", "Math Rock", "Experimental Rock",
                "Art Rock", "Krautrock", "Space Rock", "Stoner Rock", "Desert Rock"
            )
        ),
        GenreCategory(
            name = "Hardcore & Core",
            keywords = listOf(
                "Hardcore", "Metalcore", "Deathcore", "Grindcore", "Mathcore",
                "Post-Hardcore", "Melodic Hardcore", "Hardcore Punk", "Beatdown Hardcore",
                "Crossover", "Powerviolence", "Fastcore", "Crustcore", "Sludgecore",
                "Noisecore", "Goregrind", "Pornogrind", "Mincecore", "Thrashcore"
            )
        ),
        GenreCategory(
            name = "Electronic & Industrial",
            keywords = listOf(
                "Electronic", "Industrial", "Industrial Metal", "Industrial Rock",
                "EBM", "Dark Electro", "Synthwave", "Darkwave", "Coldwave",
                "Ambient", "Dark Ambient", "Drone", "Noise", "Power Electronics",
                "Harsh Noise", "Dungeon Synth", "Martial Industrial", "Neofolk Electronic",
                "Cyber Metal", "Digital Hardcore", "Breakcore", "IDM", "Techno"
            )
        ),
        GenreCategory(
            name = "Punk",
            keywords = listOf(
                "Punk", "Hardcore Punk", "Crust Punk", "D-Beat", "Street Punk",
                "Oi!", "Post-Punk", "Anarcho-Punk", "Celtic Punk", "Folk Punk",
                "Ska Punk", "Pop Punk", "Melodic Punk", "Horror Punk", "Psychobilly"
            )
        ),
        GenreCategory(
            name = "Experimental & Avant-garde",
            keywords = listOf(
                "Experimental", "Avant-garde", "Free Jazz", "Improvisation",
                "Musique Concrète", "Sound Art", "Field Recording", "Microsound",
                "Lowercase", "Onkyokei", "Reductionism", "Extended Technique",
                "Prepared Instruments", "Circuit Bending", "Glitch", "Plunderphonics"
            )
        )
    )
    
    fun categorizeGenre(genre: String): String {
        val normalizedGenre = genre.trim()
        
        for (category in categories) {
            for (keyword in category.keywords) {
                if (normalizedGenre.contains(keyword, ignoreCase = true)) {
                    return category.name
                }
            }
        }
        
        return "Other"
    }
    
    fun getGenresForCategory(allGenres: List<String>, categoryName: String): List<String> {
        return if (categoryName == "Other") {
            allGenres.filter { genre ->
                categories.none { category ->
                    category.keywords.any { keyword ->
                        genre.contains(keyword, ignoreCase = true)
                    }
                }
            }.sorted()
        } else {
            val category = categories.find { it.name == categoryName }
            category?.let { cat ->
                allGenres.filter { genre ->
                    cat.keywords.any { keyword ->
                        genre.contains(keyword, ignoreCase = true)
                    }
                }.sorted()
            } ?: emptyList()
        }
    }
    
    fun getCategoryNames(): List<String> {
        return categories.map { it.name } + "Other"
    }
}