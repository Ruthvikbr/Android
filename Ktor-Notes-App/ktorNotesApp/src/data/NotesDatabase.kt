package com.notesapp.data

import com.notesapp.data.collection.Note
import com.notesapp.data.collection.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("NotesDatabase0")
private val Users = database.getCollection<User>()
private val Notes = database.getCollection<Note>()

suspend fun registerUser(user: User):Boolean{
    return Users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email:String):Boolean{
    return Users.findOne(User::email eq email) != null
}