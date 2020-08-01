package no.echokarriere.user

import org.koin.dsl.module

val userModule = module {
    single { UserRepository(get()) }
    single { UserQueryResolver(get()) }
    single { UserMutationResolver(get()) }
}
