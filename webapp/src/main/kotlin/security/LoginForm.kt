package kickstart.security

import com.vtence.molecule.Request
import kickstart.get
import kickstart.security.errors.login
import kickstart.validation.ValidationResult
import kickstart.validation.plus


class LoginForm(val username: String?, private val password: String?) {

    fun validate(): ValidationResult<Credentials> =
        login.username.required(username) + login.password.required(password)

    companion object {
        fun parse(request: Request): LoginForm {
            return LoginForm(request["username"], request["password"])
        }
    }
}