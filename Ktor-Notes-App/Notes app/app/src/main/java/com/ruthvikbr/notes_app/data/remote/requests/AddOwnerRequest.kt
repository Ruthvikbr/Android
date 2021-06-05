package com.ruthvikbr.notes_app.data.remote.requests

data class AddOwnerRequest(
    val noteId:String,
    val owner:String
)
