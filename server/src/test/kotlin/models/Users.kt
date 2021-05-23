package kickstart.models

data class Identity(val username: String, val password: String)

object Users {
    val bob = Identity("bob", "secret")
}
