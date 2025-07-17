// ===== app/src/main/java/com/example/gigmatcher/model/Band.kt =====
package com.simonanger.gigmatcher.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Band(
    val id: String,
    val name: String,
    val url: String,
    val genres: List<String>,
    val location: String,
    val country: String,
    val status: String = "Active"
) : Parcelable