package no.echokarriere.backend

import com.expediagroup.graphql.directives.KotlinDirectiveWiringFactory
import no.echokarriere.backend.graphql.CustomDirectiveWiringFactory
import no.echokarriere.backend.graphql.CustomSchemaGeneratorHooks
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
    @Bean
    fun wiringFactory() = CustomDirectiveWiringFactory()

    @Bean
    fun hooks(wiringFactory: KotlinDirectiveWiringFactory) = CustomSchemaGeneratorHooks(wiringFactory)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
