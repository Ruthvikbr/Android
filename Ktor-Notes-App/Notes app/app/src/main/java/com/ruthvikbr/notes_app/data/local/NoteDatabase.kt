package com.ruthvikbr.notes_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ruthvikbr.notes_app.data.local.entities.Note

@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}