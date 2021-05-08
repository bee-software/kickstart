package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.WebServer
import com.vtence.molecule.middlewares.ApacheCommonLogger
import com.vtence.molecule.middlewares.DateHeader
import com.vtence.molecule.middlewares.ServerHeader
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths
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
            .add(staticAssets(Paths.get(config[Settings.www.root])))
            .start(WebApp(config))
    }

    fun stop() {
        webServer.stop()
    }

    private fun errorLogger(error: Throwable) {
        logger.log(Level.SEVERE, "Internal server error", error)
    }
}


fun Server.resolve(path: String) = resolve(URI.create(path))

private fun staticAssets(root: Path) = assets(root.resolve("assets")) {
    serve("/favicon", "/apple-touch-icon", "/safari-pinned-tab", "/android-chrome")
    serve("/css")
    serve("/fomantic")
}




