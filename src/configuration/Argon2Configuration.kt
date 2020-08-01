package no.echokarriere.configuration

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import de.mkammerer.argon2.Argon2Helper
import io.ktor.config.HoconApplicationConfig
import io.ktor.util.KtorExperimentalAPI

class Argon2Configuration @KtorExperimentalAPI constructor(config: HoconApplicationConfig) {
    private val argon2: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
    private var iterations = when (config.propertyOrNull("prod") == null) {
        false -> Argon2Helper.findIterations(argon2, 1000, 65536, 1)
        true -> 1
    }

    fun hash(password: CharArray): String = try {
        argon2.hash(iterations, 65536, 1, password)
    } finally {
        argon2.wipeArray(password)
    }

    fun verify(hash: String, password: CharArray): Boolean = try {
        argon2.verify(hash, password)
    } finally {
        argon2.wipeArray(password)
    }
}
