package kickstart.security

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
    val authenticator: Authenticator = Authenticator { (username, password) ->
        password.takeIf { it == "secret" }?.let { User(username) }
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
        fillForm(username = "alice", password = "secret")

        val response = sessions.create(request)

        //...
        assertThat(response).isRedirectedTo("/").isDone
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
        fillForm(username = "", password = "")

        val response = sessions.create(request)

        assertThat(response).hasStatus(HttpStatus.OK)
            .isDone and view renderedWith
                Login("") +
                errors.login.username.required("") +
                errors.login.password.required("")
    }


    private fun fillForm(username: String, password: String) {
        request["username"] = username
        request["password"] = password
    }
}