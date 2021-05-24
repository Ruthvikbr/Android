package com.notesapp.routes

import com.notesapp.data.checkIfUserExists
import com.notesapp.data.collection.User
import com.notesapp.data.registerUser
import com.notesapp.data.requests.AccountRequests
import com.notesapp.data.responses.SimpleResponse
import io.ktor.application.call
import io.ktor.features.ContentTransformationException
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.registerRoute(){
    route("/register"){
        post {
            val request = try {
                call.receive<AccountRequests>()
            } catch(e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if(!userExists){
                if(registerUser(User(request.email,request.password))){
                    call.respond(OK, SimpleResponse(true,"Account successfully created"))
                }else{
                    call.respond(OK,SimpleResponse(true,"Unknown Error occurred"))
                }
            }else{
                call.respond(OK,SimpleResponse(true,"Account already exists"))
            }

        }
    }
}