package com.ruthvikbr.notes_app.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruthvikbr.notes_app.data.local.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE from notes WHERE id= :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("DELETE from notes WHERE isSynced = 1")
    suspend fun deleteAllSyncedNotes()

    @Query("SELECT * from notes WHERE id = :noteId")
    fun observeNoteById(noteId: String): LiveData<Note>

    @Query("SELECT * from notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): Note?

    @Query("SELECT * from notes ORDER BY date DESC")
    fun getAllNotes():Flow<List<Note>>

    @Query("SELECT * from notes WHERE isSynced = 0")
    suspend fun getAllUnSyncedNotes() : List<Note>
}