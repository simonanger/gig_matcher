package com.simonanger.gigmatcher.model

import android.os.Parcelable
import com.simonanger.gigmatcher.model.Band
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gig(
    val id: String,
    val title: String,
    val genre: String,
    val city: String,
    val promoterName: String,
    val matchingBands: List<Band> = emptyList(),
    val selectedBands: List<Band> = emptyList()
) : Parcelable
