package no.echokarriere.auth

import org.koin.dsl.module

val authModule = module {
    single { AuthRepository() }
    single { AuthMutationResolver(get(), get(), get(), get()) }
}
