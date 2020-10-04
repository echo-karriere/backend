package no.echokarriere

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.ResultSet
import java.util.UUID

fun ResultSet.getUUID(column: String): UUID = UUID.fromString(this.getString(column))

inline fun <reified T : Enum<T>> ResultSet.getEnumType(column: String): T {
    return enumValueOf(this.getString(column))
}

suspend fun <T> jdbiQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        block()
    }
