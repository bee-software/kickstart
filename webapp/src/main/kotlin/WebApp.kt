package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.i18n.i18n
import kickstart.security.Authenticator
import kickstart.security.SessionsController
import kickstart.security.User
import java.util.*

class WebApp(config: Configuration) : Application {

    private val pages = pages(config[Settings.www.root])
    private val i18n = i18n(config[Settings.www.lang], Locale.CANADA_FRENCH, Locale.CANADA)

    override fun handle(request: Request): Response {
        val views = i18n.localize(pages, request.attribute())

        val authenticator = Authenticator {
                (username, password) -> password.takeIf { it == "secret" }?.let { User(username) }
        }
        val sessions = SessionsController(authenticator, views.named("sessions/new"))

        val router = draw {
            get("/login").to(sessions::new)
            post("/login").to(sessions::create)

            get("/").to { views.named<Home>("home").render(Home()).done() }
        }

        return router.handle(request)
    }
}