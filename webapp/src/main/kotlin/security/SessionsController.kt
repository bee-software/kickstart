package kickstart.security

import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.View

class SessionsController(
    private val authenticator: Authenticator,
    private val view: View<Login>
) {

    fun new(request: Request): Response {
        return view.render(Login.empty).done()
    }

    fun create(request: Request): Response {
        val form: LoginForm = LoginForm.parse(request)
        val user = authenticate(form)
        if (user == null) {
            return view.render(Login.invalid(form.username)).done()
        }

        return Response.redirect("/").done()
    }

    private fun authenticate(form: LoginForm) =
        authenticator.authenticate(*form.credentials)
}