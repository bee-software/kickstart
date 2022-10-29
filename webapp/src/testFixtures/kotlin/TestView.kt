package kickstart

import com.vtence.molecule.Response

class TestView<T : Any>(private val response: Response = Response.ok()) : View<T> {
    var context: T? = null

    override fun render(model: T): Response {
        this.context = model
        return response
    }
}