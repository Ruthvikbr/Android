package com.notesapp

import com.notesapp.data.checkPasswordForEmail
import com.notesapp.routes.NoteRoutes
import com.notesapp.routes.loginRoute
import com.notesapp.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
    install(Routing) {
        registerRoute()
        loginRoute()
        NoteRoutes()
    }
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Note server"
        validate { credentials ->
            val name = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(name, password)) {
                UserIdPrincipal(name)
            } else null
        }
    }
}

