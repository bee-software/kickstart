package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.i18n.i18n
import kickstart.i18n.locale
import kickstart.security.*
import kickstart.telemetry.StatusEndpoint
import java.util.*

class WebApp(config: Configuration) : Application {

    private val pages = pages(config[Settings.www.root])
    private val i18n = i18n(config[Settings.www.lang], Locale.CANADA_FRENCH, Locale.CANADA)

    override fun handle(request: Request): Response {
        val views = i18n.localize(pages, checkNotNull(request.locale))

        val authenticator = EmailPasswordAuthenticator(object : UserBase {
            override fun findBy(username: Username): User {
                return User(username, PasswordHash.create("secret") )
            }
        })

        val status = StatusEndpoint()
        val sessions = SessionsController(authenticator, views.named("sessions/new"))
        val home = HomeController(views.named("home"))

        val router = draw {
            get("/login").to { sessions.new(it) }
            post("/login").to { sessions.create(it) }
            delete("/logout").to { sessions.delete(it) }
            get("/status").to { status.get(it)}
            get("/").to { home.render(it) }
        }

        return router.handle(request)
    }
}
