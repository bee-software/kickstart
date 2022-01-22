package kickstart.security

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import org.junit.jupiter.api.Test
import security.UserThat

class EmailPasswordAuthenticatorTest {
    val users = InMemoryUsers()
    val authenticator = EmailPasswordAuthenticator(users)

    @Test
    fun `rejects credentials when no username provided`() {
        assertThat(authenticator.authenticate(), absent())
    }

    @Test
    fun `rejects credential when no password provided`() {
        assertThat(authenticator.authenticate("username"), absent())
    }

    @Test
    fun `rejects credentials when no users have matching credentials`() {
        val authenticated = authenticator.authenticate("unknown", "password")
        assertThat("authenticated", authenticated, absent())
    }

    @Test
    fun `accepts credentials if passwords match`() {
        users.add(User(Username("other"), PasswordHash.create("secret")))
        users.add(User(Username("admin"), PasswordHash.create("secret")))

        val authenticated = authenticator.authenticate("admin", "secret")
        assertThat("authenticated", authenticated, present(UserThat.hasUsername("admin")))
    }

    @Test
    fun `rejects credentials if passwords do not match`() {
        users.add(User(Username("admin"), PasswordHash.create("secret")))

        val authenticated = authenticator.authenticate("admin", "wrong secret")
        assertThat("authenticated", authenticated, absent())
    }
}