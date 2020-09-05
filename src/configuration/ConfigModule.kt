package no.echokarriere.configuration

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import org.koin.dsl.module

val configModule = module(createdAtStart = true) {
    single { HoconApplicationConfig(ConfigFactory.load()) }
    single { Argon2Configuration(get()) }
}
