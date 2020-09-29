package no.echokarriere.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import io.ktor.application.ApplicationCall

data class ApplicationCallContext(val call: ApplicationCall) : GraphQLContext
