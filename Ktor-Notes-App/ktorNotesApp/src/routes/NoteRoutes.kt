package com.notesapp.routes


import com.notesapp.data.*
import com.notesapp.data.collection.Note
import com.notesapp.data.requests.AddOwnerRequest
import com.notesapp.data.requests.DeleteNoteRequest
import com.notesapp.data.responses.SimpleResponse

import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.noteRoute() {
    route("/getNotes", HttpMethod.Get) {
        authenticate {
            handle {
                val email = call.principal<UserIdPrincipal>()!!.name
                val notes = getNotesForEmail(email)
                call.respond(OK, notes)
            }
        }
    }
    route("/deleteNote") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteNoteRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (deleteNotes(email, request.id)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
    route("/addNote") {
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (saveNotes(note)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/addOwnerToRoute") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.owner)) {
                    call.respond(OK, SimpleResponse(false, "No user with this email exists"))
                    return@post
                }
                if(checkOwnerExists(request.owner,request.noteId)){
                    call.respond(OK, SimpleResponse(false, "User is already added to this note"))
                    return@post
                }
                if(addOwnersToNotes(request.noteId,request.owner)){
                    call.respond(OK, SimpleResponse(true, "${request.owner} can now see this note"))
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
}