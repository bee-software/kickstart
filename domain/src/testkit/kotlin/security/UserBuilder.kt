package security

import kickstart.Builder
import kickstart.security.User

class UserBuilder(
    var username: String = "john.doe",
    var password: String = "secret"
): Builder<User> {
    override fun build(): User {
        return User(username, password)
    }
}

fun user(build: UserBuilder.() -> Unit) = UserBuilder().apply(build)