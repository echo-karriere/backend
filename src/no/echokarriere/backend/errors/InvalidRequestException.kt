package no.echokarriere.backend.errors

data class InvalidRequestException(override val message: String) : RuntimeException(message)
