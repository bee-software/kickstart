package kickstart.models

data class Identity(val email: String, val password: String)

object Users {
    val bob = Identity("bob@gmail.com", "secret")
}
