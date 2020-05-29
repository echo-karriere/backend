package no.echokarriere.backend.routing

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import no.echokarriere.backend.database.IDatabase

fun Routing.apiRouter(database: IDatabase) {
    route("/api") {
        get("/") {
            call.respondText("Hello, world!")
        }
        namespaceRouter(database.namespaceDao)
    }
}
