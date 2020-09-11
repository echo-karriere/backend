package no.echokarriere

import io.kotest.core.config.AbstractProjectConfig
import no.echokarriere.utils.TestDbContainer

class ProjectConfig : AbstractProjectConfig() {
    override fun beforeAll() {
        TestDbContainer.start()
        super.beforeAll()
    }

    override fun afterAll() {
        TestDbContainer.stop()
        super.afterAll()
    }
}
