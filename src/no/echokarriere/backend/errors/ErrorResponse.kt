package no.echokarriere.backend.errors

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
    val type: String = "error"
)
