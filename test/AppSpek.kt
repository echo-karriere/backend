package no.echokarriere

import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root

@Suppress("unused") // `appRoot` used via indirection
abstract class AppSpek(appRoot: Root.() -> Unit) : Spek(appRoot) {
    @KtorExperimentalAPI
    companion object {
        private val testDatabaseConfiguration = TestDatabaseConfiguration()
        fun <R> withApp(test: suspend TestApplicationEngine.() -> R) = withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("jwt.realm", "testing")
            }
            module(database = testDatabaseConfiguration)
        }) {
            runBlocking {
                test.invoke(this@withTestApplication)
            }
        }
    }
}
