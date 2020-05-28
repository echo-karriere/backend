package no.echokarriere.backend.routing

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import no.echokarriere.backend.pages.NamespaceDao

fun Route.pageRouter(namespaceDao: NamespaceDao) {
    route("/namespace") {
        get("/") {
            call.respond(HttpStatusCode.OK, namespaceDao.selectAll())
        }
    }
}
