package no.echokarriere.backend.routing

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import no.echokarriere.backend.Config
import no.echokarriere.backend.database.Database

fun Routing.apiRouter() {
    val config = Config(application)
    val database = Database(config.database.url, config.database.username, config.database.password)

    println(database)

    route("/api") {
        get("/") {
            call.respond(mapOf("OK" to true))
        }
        pageRouter(database.namespaceDao)
    }
}
