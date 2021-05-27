package kickstart.security

import com.vtence.molecule.Request
import kickstart.get

class LoginForm(val username: String?, private val password: String?) {

    val credentials: Array<String> get() = listOfNotNull(username, password).toTypedArray()

    companion object {
        fun parse(request: Request): LoginForm {
            return LoginForm(request["username"], request["password"])
        }
    }
}