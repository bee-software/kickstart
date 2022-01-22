package kickstart.security

import java.io.Serializable

@JvmInline
value class Username(val value: String): Serializable {
    override fun toString() = value.lowercase()
}

class User(val username: Username, private val password: PasswordHash) {

    fun checkPassword(secret: String) = password.validate(secret)

    companion object {
        operator fun invoke(username: String) = User(Username(username), PasswordHash.create("secret"))
    }
}


interface UserBase {
    fun findBy(username: Username): User?
}