package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.WebServer
import com.vtence.molecule.middlewares.*
import com.vtence.molecule.session.CookieSessionStore
import kickstart.i18n.LocaleSelector
import java.net.URI
import java.nio.file.Path

class Server(host: String, port: Int) {

    private val webServer: WebServer = WebServer.create(host, port)

    val uri: URI get() = webServer.uri()

    fun resolve(path: URI): URI = uri.resolve(path)

    fun start(config: Configuration) {
        val context = ApplicationContext.load(config)

        webServer.failureReporter(context.errorReporter)
            .add(ServerHeader("Simple/6.0.1"))
            .add(DateHeader(context.clock))
            .add(HttpMethodOverride())
            .add(ApacheCommonLogger(context.logger, context.clock, context.defaultLocale))
            .add(assets(context.root))
            .add(Failsafe())
            .add(FailureMonitor(context.errorReporter))
            .add(ConnectionScope(context.dataSource))
            .add(Cookies())
            .add(LocaleSelector
                .usingDefaultLocale(context.defaultLocale)
                .alsoSupporting(context.supportedLocales))
            .add(CookieSessionTracker(CookieSessionStore.secure(context.sessionKey)))
            .add(PublicExceptions(context.root))
            .start(WebApp(context))
    }

    fun stop() {
        webServer.stop()
    }
}


fun Server.resolve(path: String) = resolve(URI.create(path))


