package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.i18n.LocalizedContent
import kickstart.i18n.i18n
import java.util.*

class WebApp(config: Configuration) : Application {

    private val pages = pages(config[Settings.www.root])
    private val i18n = i18n(config[Settings.www.lang], Locale.CANADA_FRENCH, Locale.CANADA)

    override fun handle(request: Request): Response {
        val views = i18n.localize(pages, request.attribute())

        val router = draw {
            get("/").to { views.named<LocalizedContent>("home").render(LocalizedContent()) }
        }

        return router.handle(request)
    }
}