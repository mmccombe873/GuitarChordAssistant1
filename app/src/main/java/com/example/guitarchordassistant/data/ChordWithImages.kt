package com.example.guitarchordassistant.data

import androidx.room.Embedded
import androidx.room.Relation

data class ChordWithImages(
    @Embedded val chord: Chord,
    @Relation(
        parentColumn = "chordId",
        entityColumn = "chordId"
    )
    val images: List<Image>
)