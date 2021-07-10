package com.ruthvikbr.notes_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocallyDeletedNoteId(
    @PrimaryKey(autoGenerate = false)
    val deletedNoteId:String
)
