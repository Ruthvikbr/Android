package com.ruthvikbr.notes_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ruthvikbr.notes_app.data.local.entities.LocallyDeletedNoteId
import com.ruthvikbr.notes_app.data.local.entities.Note

@Database(entities = [Note::class,LocallyDeletedNoteId::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}