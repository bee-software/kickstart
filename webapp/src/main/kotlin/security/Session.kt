package kickstart.security

import com.vtence.molecule.Request
import com.vtence.molecule.session.Session
import java.io.Serializable


val Request.session: Session get() = checkNotNull(Session.get(this)) { "no session" }


operator fun Session.set(key: Any, value: Serializable?) {
    put<Any?>(key, value)
}


fun bindFreshSession(request: Request): Session {
    return Session().also { it.bind(request) }
}


private const val USER_ID = "user.id"

var Session.username: Username?
    get() = this[USER_ID]
    set(name) = if (name != null) this[USER_ID] = name else remove(USER_ID)

val Session.isLoggedIn get() = username != null
