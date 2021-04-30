package kickstart

import com.vtence.molecule.Application
import com.vtence.molecule.Middleware
import com.vtence.molecule.MiddlewareStack
import com.vtence.molecule.middlewares.FilterMap
import com.vtence.molecule.middlewares.Router
import com.vtence.molecule.routing.RouteBuilder
import com.vtence.molecule.routing.RouteSet
import com.vtence.molecule.routing.Routes


fun stack(builder: MiddlewareStack.() -> Unit): Application = MiddlewareStack().apply(builder).boot()

fun MiddlewareStack.filter(prefix: String, filter: Middleware): MiddlewareStack = use(FilterMap().map(prefix, filter))

fun MiddlewareStack.draw(definition: Routes.() -> Unit): MiddlewareStack = run(Router.draw(routes(definition)))


fun draw(definition: Routes.() -> Unit): Application = Router.draw(routes(definition))

fun routes(definition: Routes.() -> Unit): RouteBuilder = Routes().apply(definition)

infix fun RouteBuilder.then(others: RouteBuilder): RouteBuilder = CombinedRoutes(this, others)


private class CombinedRoutes(private val first: RouteBuilder, private val second: RouteBuilder) : RouteBuilder {
    override fun build(routes: RouteSet?) {
        first.build(routes)
        second.build(routes)
    }
}
