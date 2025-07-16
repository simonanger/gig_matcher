// ===== app/src/main/java/com/example/gigmatcher/model/Gig.kt =====
package com.example.gigmatcher.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gig(
    val id: String,
    val title: String,
    val genre: String,
    val city: String,
    val promoterName: String,
    val matchingBands: List<Band> = emptyList()
) : Parcelable