package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.vtence.molecule.Request.get
import com.vtence.molecule.Request.post
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import kickstart.TestView
import kickstart.and
import kickstart.set
import kotlin.test.Test

class SessionsControllerTest {

    val view = TestView<Login>()
    val authenticator: Authenticator = Authenticator { (email, password) ->
        password.takeIf { it == "secret" }?.let { User(email) }
    }
    val sessions = SessionsController(authenticator, view)

    val request = post("/")

    @Test
    fun `renders login page to open session`() {
        val response = sessions.new(get("/"))

        assertThat(response)
            .hasStatus(HttpStatus.OK)
            .isDone and
                view renderedWith Login.empty
    }

    @Test
    fun `creates fresh session when login successful`() {
        fillForm(email = "alice@gmail.com", password = "secret")

        freshSession(request).invalidate()
        val response = sessions.create(request)

        assertThat(response).isRedirectedTo("/").isDone

        val user = request.session.username
        assertThat("signed in email", user, equalTo(Username("alice@gmail.com")))
    }

    @Test
    fun `renders invalid credentials when authentication fails`() {
        fillForm("chris", "wrong secret")

        val response = sessions.create(request)

        assertThat(response)
            .hasStatus(HttpStatus.OK)
            .isDone and
                view renderedWith Login.invalid("chris")
    }

    @Test
    fun `renders form errors when form validation fails`() {
        fillForm(email = "", password = "")

        val response = sessions.create(request)

        assertThat(response).hasStatus(HttpStatus.OK)
            .isDone and view renderedWith
                Login("") +
                errors.login.email.required("") +
                errors.login.password.required("")
    }


    private fun fillForm(email: String, password: String) {
        request["email"] = email
        request["password"] = password
    }
}
