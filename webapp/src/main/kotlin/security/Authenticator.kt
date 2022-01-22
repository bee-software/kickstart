package kickstart.security


typealias Credential = String

typealias Credentials = List<Credential>

fun interface Authenticator {
    fun authenticate(credentials: Credentials): User?
}

fun Authenticator.authenticate(vararg credentials: Credential) = authenticate(credentials.toList())
