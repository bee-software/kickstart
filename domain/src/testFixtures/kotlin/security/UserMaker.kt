package kickstart.security

import com.vtence.konstruct.Factory
import com.vtence.konstruct.property

object UserMaker {
    val username = property<User, String>()
    val password = property<User, String>()

    val user = Factory {
        User(
            username = it.valueOf(username) ?: "john.doe",
            password = it.valueOf(password) ?: "secret"
        )
    }
}