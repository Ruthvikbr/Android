package com.example.noteapp.feature_note.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNotes: DeleteNote,
    val addNote: AddNote,
    val getNote:GetNote
)
