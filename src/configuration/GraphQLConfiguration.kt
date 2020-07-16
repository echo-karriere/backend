package no.echokarriere.configuration

import com.expediagroup.graphql.SchemaGeneratorConfig
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.toSchema
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.content.defaultResource
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KType
import no.echokarriere.namespace.NamespaceMutationResolver
import no.echokarriere.namespace.NamespaceQueryResolver
import no.echokarriere.namespace.NamespaceRepository

data class GraphQLRequest(val query: String, val operationName: String?, val variables: Map<String, Any>?)

fun Application.installGraphQL(namespaceRepository: NamespaceRepository) {
    val config = SchemaGeneratorConfig(
        supportedPackages = listOf("no.echokarriere"),
        hooks = CustomSchemaGeneratorHooks()
    )
    val queries = listOf(TopLevelObject(NamespaceQueryResolver(namespaceRepository)))
    val mutations = listOf(TopLevelObject(NamespaceMutationResolver(namespaceRepository)))

    val schema = toSchema(config, queries, mutations)
    val graphql = GraphQL.newGraphQL(schema).build()

    suspend fun ApplicationCall.executeQuery() {
        val request = receive<GraphQLRequest>()
        val executionInput = ExecutionInput.newExecutionInput()
            // .context(ApplicationCallContext(this))
            .query(request.query)
            .operationName(request.operationName)
            .variables(request.variables)
            .build()

        respond(graphql.execute(executionInput))
    }

    routing {
        post("/graphql") {
            call.executeQuery()
        }

        static("playground") {
            defaultResource("static/playground.html")
        }
    }
}

class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        UUID::class -> graphQLUuidType
        OffsetDateTime::class -> graphqlDateTimeType
        else -> null
    }
}

internal val graphQLUuidType: GraphQLScalarType = GraphQLScalarType.newScalar()
    .name("UUID")
    .description("A type representing a formatted java.util.UUID")
    .coercing(UUIDCoercing)
    .build()

internal val graphqlDateTimeType: GraphQLScalarType = ExtendedScalars.DateTime

private object UUIDCoercing : Coercing<UUID, String> {
    override fun parseValue(input: Any?): UUID = UUID.fromString(serialize(input))

    override fun parseLiteral(input: Any?): UUID? {
        val uuidString = (input as? StringValue)?.value
        return UUID.fromString(uuidString)
    }

    override fun serialize(dataFetcherResult: Any?): String = dataFetcherResult.toString()
}
