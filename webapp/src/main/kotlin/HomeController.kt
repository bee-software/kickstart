package kickstart

import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.security.session
import kickstart.security.username

class HomeController(private val view: View<Home>) {
    fun render(request: Request): Response {
        return view.render(Home(request.session.username)).done()
    }
}
