package no.echokarriere.backend

import com.expediagroup.graphql.spring.operations.Query
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Component
class HelloQueryResolver : Query {
    fun hello(greeting: String): Hello = Hello(greeting)
}

data class Hello(val hello: String)
