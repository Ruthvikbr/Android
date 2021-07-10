package com.notesapp.data

import com.notesapp.data.collection.Note
import com.notesapp.data.collection.User
import com.notesapp.security.checkHashForPassword
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.not
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("NotesDatabase0")
private val Users = database.getCollection<User>()
private val Notes = database.getCollection<Note>()

suspend fun registerUser(user: User): Boolean {
    return Users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return Users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, password: String): Boolean {
    val actualPassword = Users.findOne(User::email eq email)?.password ?: return false
    return checkHashForPassword(password,actualPassword)
}

suspend fun getNotesForEmail(email: String): List<Note> {
    return Notes.find(Note::owners contains email).toList()
}

suspend fun saveNotes(note: Note): Boolean {
    val noteExists = Notes.findOneById(note.id) != null
    return if (noteExists) {
        Notes.updateOneById(note.id, note).wasAcknowledged()
    } else {
        Notes.insertOne(note).wasAcknowledged()
    }
}

suspend fun deleteNotes(email: String, noteId: String): Boolean {
    val note = Notes.findOne(Note::id eq noteId, Note::owners contains email)
    note?.let {
        if (note.owners.size > 1) {
            val newOwners = note.owners - email
            val updateResult = Notes.updateOne(Note::id eq note.id, setValue(Note::owners, newOwners))
            return updateResult.wasAcknowledged()
        }
        return Notes.deleteOne(note.id).wasAcknowledged()
    } ?: return false
}

suspend fun checkOwnerExists(owner: String,noteId: String):Boolean{
    val note = Notes.findOneById(noteId)?:return false
    return owner in note.owners
}

suspend fun addOwnersToNotes(noteId: String,owner:String):Boolean{
    val owners = Notes.findOneById(noteId)?.owners?:return false
    return Notes.updateOneById(noteId, setValue(Note::owners,owners+owner)).wasAcknowledged()
}