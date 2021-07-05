package com.ruthvikbr.notes_app.repositories

import android.app.Application
import com.ruthvikbr.notes_app.data.local.NoteDao
import com.ruthvikbr.notes_app.data.local.entities.Note
import com.ruthvikbr.notes_app.data.remote.NoteApi
import com.ruthvikbr.notes_app.data.remote.requests.AccountRequest
import com.ruthvikbr.notes_app.utils.Resource
import com.ruthvikbr.notes_app.utils.checkNetworkConnection
import com.ruthvikbr.notes_app.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {
    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.register(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Something went wrong. Please try again", null)
        }
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.register(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Something went wrong. Please try again", null)
        }
    }

    fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                noteApi.getNotes()
            },
            saveFetchResult = { response ->
                response.body()?.let {
                    insertNotes(it)
                }
            },
            shouldFetch = {
                checkNetworkConnection(context)
            }
        )
    }

    suspend fun insertNote(note: Note) {
        val response = try {
            noteApi.addNote(note)
        } catch (e: Exception) {
            null
        }

        if (response != null && response.isSuccessful) {
            noteDao.insertNote(note.apply { isSynced = true })
        } else {
            noteDao.insertNote(note)
        }
    }

    suspend fun insertNotes(notes:List<Note>){
        notes.forEach {
           insertNote(it)
        }
    }

    suspend fun getNotesById(id:String) = noteDao.getNoteById(id)
}