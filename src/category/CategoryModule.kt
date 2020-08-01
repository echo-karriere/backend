package no.echokarriere.category

import org.koin.dsl.module

val categoryModule = module {
    single { CategoryRepository() }
    single { CategoryMutationResolver(get()) }
    single { CategoryQueryResolver(get()) }
}
