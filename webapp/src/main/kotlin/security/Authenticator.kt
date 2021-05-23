package kickstart.security

fun interface Authenticator {

    fun authenticate(vararg credentials: String): User?
}