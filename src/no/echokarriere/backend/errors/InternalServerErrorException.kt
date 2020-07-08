package no.echokarriere.backend.errors

import kotlinx.serialization.Serializable

@Serializable
data class InternalServerErrorException(override val message: String = "Whoops, something went wrong") :
    RuntimeException(message)
