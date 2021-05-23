package kickstart.security

import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.View
import kickstart.get

class SessionsController(
    private val authenticator: Authenticator,
    private val view: View<Login>
) {

    fun new(request: Request): Response {
        return view.render(Login()).done()
    }

    fun create(request: Request): Response {
        val user = authenticate(request["username"], request["password"])

        if (user == null) {
            return view.render(Login(request["username"])).done()
        }

        return Response.redirect("/").done()
    }

    private fun authenticate(username: String?, password: String?) =
        authenticator.authenticate(*listOfNotNull(username, password).toTypedArray())
}