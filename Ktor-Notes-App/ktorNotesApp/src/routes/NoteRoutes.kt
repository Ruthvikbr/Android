package com.notesapp.routes


import com.notesapp.data.getNotesForEmail

import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.noteRoute(){
    route("/getNotes", HttpMethod.Get) {
        authenticate {
           handle {
               val email = call.principal<UserIdPrincipal>()!!.name
               val notes = getNotesForEmail(email)
               call.respond(OK, notes)
           }
        }
    }
}