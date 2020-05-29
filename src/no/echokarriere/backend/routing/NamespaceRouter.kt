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
import no.echokarriere.backend.database.NamespaceDao
import no.echokarriere.backend.entities.Namespace
import no.echokarriere.backend.errors.InternalServerErrorException
import no.echokarriere.backend.errors.InvalidRequestException
import no.echokarriere.backend.errors.NotFoundException

fun Route.namespaceRouter(namespaceDao: NamespaceDao) {
    route("/namespace") {
        get {
            call.respond(HttpStatusCode.OK, namespaceDao.selectAll())
        }
        get("{id}") {
            val id: Int = call.parameters["id"]?.toInt() ?: throw InvalidRequestException("ID required")
            val namespace = namespaceDao.selectOne(id) ?: throw NotFoundException("No namespace matching $id found")
            call.respond(HttpStatusCode.OK, namespace)
        }
        post {
            val namespace = call.receive<Namespace>()
            val resp = try {
                namespaceDao.insert(namespace)
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.NoContent)
            }
            call.respond(HttpStatusCode.Created, resp)
        }
        put("{id}") {
            val id: Int = call.parameters["id"]?.toInt() ?: throw InvalidRequestException("ID required")
            val namespace = call.receive<Namespace>()
            val resp = namespaceDao.update(id, namespace) ?: throw NotFoundException("No namespace matching $id found")
            call.respond(HttpStatusCode.OK, resp)
        }
        delete("{id}") {
            val id: Int = call.parameters["id"]?.toInt() ?: throw InvalidRequestException("ID required")
            when (namespaceDao.delete(id)) {
                0 -> call.respond(HttpStatusCode.NotFound)
                1 -> call.respond(HttpStatusCode.NoContent)
                else -> throw InternalServerErrorException()
            }
        }
    }
}
