package kickstart.security


class EmailPasswordAuthenticator(private val users: UserBase): Authenticator {

    override fun authenticate(credentials: Credentials): User? {
        val username = credentials.firstOrNull() ?: return null
        val password = credentials.drop(1).firstOrNull() ?: return null

        val candidate = users.findBy(username = Username(username))
        return candidate?.takeIf { it.checkPassword(password) }
    }
}
