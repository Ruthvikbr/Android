package com.ruthvikbr.notes_app.ui.notes

import androidx.lifecycle.*
import com.ruthvikbr.notes_app.data.local.entities.Note
import com.ruthvikbr.notes_app.repositories.NoteRepository
import com.ruthvikbr.notes_app.utils.Event
import com.ruthvikbr.notes_app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val forceUpdate = MutableLiveData<Boolean>(false)
    private val _allNotes = forceUpdate.switchMap {
        repository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }

    val allNotes: LiveData<Event<Resource<List<Note>>>> = _allNotes

    fun syncAllNotes() = forceUpdate.postValue(true)

    fun deleteLocallyDeletedNoteId(noteID:String)=viewModelScope.launch {
        repository.deleteLocallyDeletedNoteId(noteID)
    }

    fun deleteNote(noteId:String) =viewModelScope.launch {
        repository.deleteNote(noteId)
    }

    fun insertNote (note:Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

}