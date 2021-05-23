package kickstart.security

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.present
import com.vtence.molecule.Request.get
import com.vtence.molecule.Request.post
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import kickstart.TestView
import kickstart.ViewAssert.Companion.assertThat
import kickstart.set
import org.junit.jupiter.api.Test

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

        assertThat(view).renderedWith(present())
        assertThat(response).hasStatus(HttpStatus.OK).isDone
    }

    @Test
    fun `creates fresh session when login successful`() {
        fillForm(username = "alice", password = "secret")

        val response = sessions.create(request)

        //...
        assertThat(response).isRedirectedTo("/").isDone
    }

    @Test
    fun `renders form errors when form is invalid`() {
        fillForm("chris", "wrong secret")

        val response = sessions.create(request)

        assertThat(view).renderedWith(has(Login::username, equalTo("chris")))
        assertThat(response).hasStatus(HttpStatus.OK).isDone
    }

    private fun fillForm(username: String, password: String) {
        request["username"] = username
        request["password"] = password
    }
}