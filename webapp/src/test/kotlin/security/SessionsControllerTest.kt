package kickstart.security

import com.vtence.molecule.Request.get
import com.vtence.molecule.Request.post
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import kickstart.TestView
import kickstart.and
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
    fun `renders form errors when authentication fails`() {
        fillForm("chris", "wrong secret")

        val response = sessions.create(request)

        assertThat(response)
            .hasStatus(HttpStatus.OK)
            .isDone and
                view renderedWith Login.invalid("chris")
    }

    private fun fillForm(username: String, password: String) {
        request["username"] = username
        request["password"] = password
    }
}