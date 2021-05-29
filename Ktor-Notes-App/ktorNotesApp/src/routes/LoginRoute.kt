package com.notesapp.routes

import com.notesapp.data.checkPasswordForEmail
import com.notesapp.data.requests.AccountRequests
import com.notesapp.data.responses.SimpleResponse
import io.ktor.application.call
import io.ktor.features.ContentTransformationException
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.loginRoute(){
    route("/login"){
        post {
            val request = try {
                call.receive<AccountRequests>()
            } catch(e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            val isValidLogin = checkPasswordForEmail(request.email,request.password)
            if(isValidLogin){
                call.respond(OK,SimpleResponse(true,"Successfully logged in"))
            }else{
                call.respond(OK,SimpleResponse(false,"Your Email or password is incorrect"))
            }
        }
    }
}