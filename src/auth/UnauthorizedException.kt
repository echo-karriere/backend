package no.echokarriere.auth

import java.lang.Exception

data class UnauthorizedException(override val message: String) : Exception(message)
