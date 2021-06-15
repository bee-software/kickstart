package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.Application
import com.vtence.molecule.Response
import com.vtence.molecule.WebServer
import com.vtence.molecule.middlewares.*
import com.vtence.molecule.session.CookieSessionStore
import kickstart.i18n.LocaleSelector
import java.net.URI
import java.nio.file.Path
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
            .add(HttpMethodOverride())
            .add(ApacheCommonLogger(logger, Clock.systemDefaultZone(), config[Settings.www.lang]))
            .add(staticAssets(config[Settings.www.root]))
            .mount("/status", diagnostics())
            .add(Cookies())
            .add(LocaleSelector.usingDefaultLocale(config[Settings.www.lang])
                .alsoSupporting(Locale.CANADA_FRENCH, Locale.CANADA))
            .add(CookieSessionTracker(CookieSessionStore.secure("super secret key")))
            .add(PublicExceptions(config[Settings.www.root]))
            .start(WebApp(config))
    }

    private fun diagnostics(): Application = Application {
        Response.ok().contentType("text/plain").done("All green.")
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

