package com.ruthvikbr.notes_app.ui.addeditnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruthvikbr.notes_app.data.local.entities.Note
import com.ruthvikbr.notes_app.repositories.NoteRepository
import com.ruthvikbr.notes_app.utils.Event
import com.ruthvikbr.notes_app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _note = MutableLiveData<Event<Resource<Note>>>()
    val note: LiveData<Event<Resource<Note>>> = _note

    fun insertNote(note: Note) = GlobalScope.launch {
        repository.insertNote(note)
    }

    fun getNoteById(id:String) = viewModelScope.launch {
        _note.postValue(Event(Resource.loading(null)))
        val note = repository.getNotesById(id)
        note?.let {
            _note.postValue(Event(Resource.success(it)))
        }?: _note.postValue(Event(Resource.error("Note not found",null)))
    }


}