package kickstart.security

import java.io.Serializable

@JvmInline
value class Username(val value: String): Serializable {
    override fun toString() = value.lowercase()
}

class User(val username: Username, private val password: PasswordHash) {

    val passwordHash: String by password::value

    fun checkPassword(secret: String) = password.validate(secret)

    companion object {
        operator fun invoke(username: String, password: String) = User(Username(username), PasswordHash.create(password))
    }
}


interface UserBase {
    fun findBy(username: Username): User?
}