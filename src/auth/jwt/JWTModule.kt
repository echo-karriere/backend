package no.echokarriere.auth.jwt

import org.koin.dsl.module

val jwtModule = module {
    single { JWTConfiguration(get()) }
}
