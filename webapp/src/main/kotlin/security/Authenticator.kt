package kickstart.security


typealias Credentials = List<String>

fun interface Authenticator {
    fun authenticate(credentials: Credentials): User?
}
