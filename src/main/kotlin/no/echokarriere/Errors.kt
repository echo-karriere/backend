package no.echokarriere

sealed class Errors {
    class SQLError(override val message: String) : Exception(message)
}
