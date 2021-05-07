package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import java.nio.file.Paths

class WebApp(private val config: Configuration) : Application {

    private val pages = Pages.inDir(Paths.get(config[Settings.www.root]).resolve("app").resolve("views"))

    override fun handle(request: Request): Response {
        val router = draw {
            get("/status").to { Response.ok().contentType("text/plain").done("All green.") }
            get("/").to { pages.named<Unit>("home").render(Unit) }
        }

        return router.handle(request)
    }
}