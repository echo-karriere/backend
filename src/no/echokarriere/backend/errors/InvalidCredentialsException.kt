package no.echokarriere.backend.errors

import kotlinx.serialization.Serializable

@Serializable
class InvalidCredentialsException(override val message: String) : RuntimeException(message)
