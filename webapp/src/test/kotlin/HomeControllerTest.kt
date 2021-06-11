package kickstart

import com.vtence.molecule.Request
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import kickstart.security.Username
import kickstart.security.bindFreshSession
import kickstart.security.username
import kotlin.test.Test

class HomeControllerTest {
    val view = TestView<Home>()
    val home = HomeController(view)

    @Test
    fun `renders home page, with currently signed in user`() {
        val request = Request.get("/")
        bindFreshSession(request).also { it.username = Username("alice@gmail.com") }

        val response = home.render(request)

        assertThat(response)
            .hasStatus(HttpStatus.OK)
            .isDone and
                view renderedWith Home(Username("alice@gmail.com"))
    }
}
