package com.notesapp.routes


import com.notesapp.data.getNotesForEmail
import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.NoteRoutes(){
    route("/getNotes"){
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val notes = getNotesForEmail(email)
                call.respond(OK,notes)
            }
        }
    }
}