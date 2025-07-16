// ===== app/src/main/java/com/example/gigmatcher/model/Band.kt =====
package com.simonanger.gigmatcher.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Band(
    val id: String,
    val name: String,
    val genres: List<String>,
    val cities: List<String>,
    val logo: String? = null,
    val bandcampLink: String? = null,
    val contact: String? = null,
    val bio: String = ""
) : Parcelable