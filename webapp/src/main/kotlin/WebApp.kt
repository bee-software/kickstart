package kickstart

import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import com.vtence.molecule.middlewares.Router

class WebApp(private val app: ApplicationContext) : Application {

    override fun handle(request: Request): Response {
        val context = app.contextualize(request)

        val router = Router.draw(
            context.security.routes then
            context.telemetry.routes then
            context.root.routes
        )

        return router.handle(request)
    }
}
