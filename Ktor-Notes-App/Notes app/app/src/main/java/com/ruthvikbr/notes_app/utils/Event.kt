package com.ruthvikbr.notes_app.utils

open class Event<out T>(private val content:T) {
    var hasBeenHandled:Boolean = false
    private set

    fun getContentIfNotHandled() = if(hasBeenHandled){
        null
    }else{
        hasBeenHandled=true
        content
    }

    fun peekContent() = content
}