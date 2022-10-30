package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.vtence.molecule.Request
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import kickstart.*

class SessionsControllerTest : JUnit5Minutests {

    class Fixture {
        val view = TestView<Login>()

        val authenticator: Authenticator = Authenticator { (email, password) ->
            password.takeIf { it == "secret" }?.let { User(email, password) }
        }
        val sessions = SessionsController(authenticator, view)

        val request = Request.get("/").also { bindFreshSession(it) }

        fun fillForm(email: String, password: String) {
            request["email"] = email
            request["password"] = password
        }
    }

    @Tests
    fun tests() = rootContext<Fixture> {
        given { Fixture() }

        test("renders login page to open session") {
            val response = sessions.new(request)

            assertThat(response)
                .hasStatus(HttpStatus.OK)
                .isDone and
                    view renderedWith Login.empty
        }

        test("destroys session and redirects to home page on logout") {
            val response = sessions.delete(request)

            assertThat(response)
                .isRedirectedTo("/")
                .isDone

            assertThat("session invalidated", request.session.invalid(), equalTo(true))
        }

        context("when session already opened") {
            beforeEach {
                request.session.username = Username("John")
            }

            test("redirects to download page") {
                val response = sessions.new(request)

                assertThat(response).isRedirectedTo("/").isDone
            }
        }

        context("successful login") {
            beforeEach {
                fillForm(email = "alice@gmail.com", password = "secret")
            }

            test("creates fresh session") {
                request.session.invalidate()
                val response = sessions.create(request)

                assertThat(response).isRedirectedTo("/").isDone

                val user = request.session.username
                assertThat("signed in email", user, equalTo(Username("alice@gmail.com")))
            }
        }

        context("authentication failure") {
            beforeEach {
                fillForm("chris", "wrong secret")
            }

            test("renders invalid credentials") {
                val response = sessions.create(request)

                assertThat(response)
                    .hasStatus(HttpStatus.OK)
                    .isDone and
                        view renderedWith Login.invalid("chris")
            }
        }

        context("failed validation") {
            beforeEach {
                fillForm(email = "", password = "")
            }

            test("renders form errors") {
                val response = sessions.create(request)

                assertThat(response).hasStatus(HttpStatus.OK)
                    .isDone and view renderedWith
                        Login("") +
                        errors.login.email.required("") +
                        errors.login.password.required("")
            }
        }
    }
}
