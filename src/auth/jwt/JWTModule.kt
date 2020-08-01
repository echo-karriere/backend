package no.echokarriere.auth.jwt

import io.ktor.util.KtorExperimentalAPI
import org.koin.dsl.module

@KtorExperimentalAPI
val jwtModule = module {
    single { JWTConfiguration(get()) }
}
