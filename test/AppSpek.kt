package no.echokarriere

import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root

@Suppress("unused") // `appRoot` used via indirection
abstract class AppSpek(appRoot: Root.() -> Unit) : Spek(appRoot) {
    companion object {
        private val testDatabaseConfiguration = TestDatabaseConfiguration()
        fun <R> withApp(test: suspend TestApplicationEngine.() -> R) = withTestApplication({
            module(testing = true, database = testDatabaseConfiguration)
        }) {
            runBlocking {
                test.invoke(this@withTestApplication)
            }
        }
    }
}
