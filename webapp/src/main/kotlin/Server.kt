package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.Response
import com.vtence.molecule.WebServer
import com.vtence.molecule.middlewares.ApacheCommonLogger
import com.vtence.molecule.middlewares.DateHeader
import com.vtence.molecule.middlewares.ServerHeader
import java.net.URI
import java.time.Clock
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class Server(host: String, port: Int) {

    private val webServer: WebServer = WebServer.create(host, port)

    val uri: URI get() = webServer.uri()

    var logger: Logger = Loggers.off()

    fun resolve(path: URI): URI = uri.resolve(path)

    fun start(config: Configuration) {
        webServer.failureReporter(this::errorLogger)
            .add(ServerHeader("Simple/6.0.1"))
            .add(DateHeader(Clock.systemDefaultZone()))
            .add(ApacheCommonLogger(logger, Clock.systemDefaultZone(), Locale.CANADA))
            .start(draw {
                get("/status").to { Response.ok().done("All green.") }
                get("/").to { Response.ok().done("It works!") }
            })
    }

    fun stop() {
        webServer.stop()
    }

    private fun errorLogger(error: Throwable) {
        logger.log(Level.SEVERE, "Internal server error", error)
    }
}


fun Server.resolve(path: String) = resolve(URI.create(path))

