package kickstart.security

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.vtence.konstruct.Provider
import com.vtence.konstruct.a
import com.vtence.konstruct.make
import com.vtence.konstruct.with
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import kickstart.security.UserMaker.password
import kickstart.security.UserMaker.user
import kickstart.security.UserMaker.username
import security.UserThat


class EmailPasswordAuthenticatorTest : JUnit5Minutests {
    class Fixture(
        private val users: InMemoryUsers = InMemoryUsers(),
        val authenticator: Authenticator = EmailPasswordAuthenticator(users),
    ) : Authenticator by authenticator {

        fun given(vararg users: Provider<User>) {
            users.forEach { this.users.add(make(it)) }
        }
    }

    @Tests
    fun tests() = rootContext<Fixture> {
        given { Fixture() }

        test("rejects credentials when no username provided") {
            assertThat(authenticate(), absent())
        }

        test("rejects credential when no password provided") {
            assertThat(authenticate("username"), absent())
        }

        context("with existing users") {
            beforeEach {
                given(
                    a(user, with(username,"other"), with(password, "secret")),
                    a(user, with(username,"admin"), with(password, "secret")),
                )
            }

            test("rejects credentials when no users have matching credentials") {
                val authenticated = authenticate("unknown", "password")
                assertThat("authenticated", authenticated, absent())
            }

            test("accepts credentials if passwords match") {
                val authenticated = authenticate("admin", "secret")
                assertThat("authenticated", authenticated, present(UserThat.hasUsername("admin")))
            }

            test("rejects credentials if passwords do not match") {
                val authenticated = authenticate("admin", "wrong secret")
                assertThat("authenticated", authenticated, absent())
            }
        }
    }
}