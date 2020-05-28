package no.echokarriere.backend.routing

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import no.echokarriere.backend.ItemNotFoundException
import no.echokarriere.backend.pages.Namespace
import no.echokarriere.backend.pages.NamespaceDao

fun Route.pageRouter(namespaceDao: NamespaceDao) {
    route("/namespace") {
        get {
            call.respond(HttpStatusCode.OK, namespaceDao.selectAll())
        }
        get("{id}") {
            val id: Int = call.parameters["id"]?.toInt() ?: error("ID required")
            val namespace = namespaceDao.selectOne(id) ?: throw ItemNotFoundException("No namespace matching $id found")
            call.respond(HttpStatusCode.OK, namespace)
        }
        post {
            val namespace = call.receive<Namespace>()
            namespaceDao.insert(namespace)
            call.respond(mapOf("OK" to true))
        }
        put("{id}") {
            val id: Int = call.parameters["id"]?.toInt() ?: error("ID required")
            val namespace = call.receive<Namespace>()
            namespaceDao.update(id, namespace)
            call.respond(mapOf("OK" to true))
        }
        delete("{id}") {
            val id: Int = call.parameters["id"]?.toInt() ?: error("ID required")
            namespaceDao.delete(id)
            call.respond(mapOf("OK" to true))
        }
    }
}
