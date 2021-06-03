package kickstart.security

import java.io.Serializable

@JvmInline
value class Username(val value: String): Serializable

class User(val username: Username) {

    companion object {
        operator fun invoke(username: String) = User(Username(username))
    }
}
