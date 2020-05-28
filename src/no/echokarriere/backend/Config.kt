@file:Suppress("EXPERIMENTAL_API_USAGE")

package no.echokarriere.backend

import io.ktor.application.Application

class Config(application: Application) {
    val database = Database(application)

    class Database(application: Application) {
        val url = application.environment.config.property("database.url").getString()
        val username = application.environment.config.property("database.username").getString()
        val password = application.environment.config.property("database.password").getString()
    }
}
