package com.ruthvikbr.notes_app.data.local

import androidx.room.Database
import com.ruthvikbr.notes_app.data.local.entities.Note

@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase {
    abstract fun noteDao(): NoteDao
}