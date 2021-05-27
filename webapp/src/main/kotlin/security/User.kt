package kickstart.security

@JvmInline
value class Username(val value: String)

class User(val username: Username) {

    companion object {
        operator fun invoke(username: String) = User(Username(username))
    }
}
