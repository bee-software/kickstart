package kickstart

import com.vtence.molecule.Request
import com.vtence.molecule.routing.RouteBuilder
import com.vtence.molecule.routing.RouteSet
import com.vtence.molecule.routing.Routes


fun routes(definition: Routes.() -> Unit): RouteBuilder = Routes().apply(definition)

infix fun RouteBuilder.then(others: RouteBuilder): RouteBuilder = CombinedRoutes(this, others)


private class CombinedRoutes(private val first: RouteBuilder, private val second: RouteBuilder) : RouteBuilder {
    override fun build(routes: RouteSet?) {
        first.build(routes)
        second.build(routes)
    }
}

inline fun <reified T> Request.attribute() : T = this.attribute(T::class.java)

operator fun Request.get(name: String): String? = this.parameter(name)

operator fun Request.set(name: String, value: String?): Request = this.addParameter(name, value)

