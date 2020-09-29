package no.echokarriere

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> jdbiQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        block()
    }
