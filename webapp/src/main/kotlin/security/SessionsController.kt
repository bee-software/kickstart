package kickstart.security

import com.vtence.molecule.Request
import com.vtence.molecule.Response
import kickstart.View
import kickstart.done
import kickstart.validation.ValidationResult.Failure
import kickstart.validation.ValidationResult.Success

class SessionsController(
    private val authenticator: Authenticator,
    private val view: View<Login>
) {

    fun new(request: Request): Response {
        return if (request.session.isLoggedIn)
            Response.redirect("/").done()
        else
            view.done(Login.empty)
    }

    fun create(request: Request): Response {
        val form = LoginForm.parse(request)

        when (val result = form.validate()) {
            is Success -> {
                val user = authenticator.authenticate(result.value) ?: return view.done(Login.invalid(form.email))
                val session = request.bindFreshSession()
                session.username = user.username
            }
            is Failure -> return view.done(Login(form.email) + result)
        }

        return Response.redirect("/").done()
    }

    fun delete(request: Request): Response {
        request.session.invalidate()
        return Response.redirect("/").done()
    }
}
