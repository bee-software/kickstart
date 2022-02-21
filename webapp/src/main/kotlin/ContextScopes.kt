package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.kabinet.StatementExecutor
import com.vtence.molecule.FailureReporter
import com.vtence.molecule.Request
import kickstart.db.DataSources
import kickstart.i18n.i18n
import kickstart.i18n.locale
import kickstart.security.SecurityModule
import kickstart.telemetry.TelemetryModule
import java.nio.file.Path
import java.time.Clock
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.sql.DataSource


class ApplicationContext private constructor(config: Configuration) {
    val root: Path = config[Settings.www.root]
    val defaultLocale: Locale = config[Settings.www.lang]
    val supportedLocales: Iterable<Locale> = listOf(Locale.CANADA_FRENCH, Locale.CANADA)
    val clock: Clock = Clock.systemDefaultZone()
    val sessionKey: String = "super secret key"
    val logger: Logger = if (config[Settings.server.quiet]) Loggers.off() else Loggers.toConsole(Level.ALL)
    val errorReporter: FailureReporter = ErrorLogger(logger)
    val dataSource: DataSource = DataSources.configure(config)

    private val pages = pages(root)
    private val i18n = i18n(defaultLocale, Locale.CANADA_FRENCH, Locale.CANADA)

    fun contextualize(request: Request) = RequestContext(
        db = StatementExecutor(request.attribute()),
        views = i18n.localize(pages, checkNotNull(request.locale))
    )

    companion object {
        fun load(config: Configuration) = ApplicationContext(config)
    }
}


class RequestContext(db: StatementExecutor, views: Views) {
    val security = SecurityModule(db, views)
    val telemetry = TelemetryModule()
    val root = RootModule(views)
}
