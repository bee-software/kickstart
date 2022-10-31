package kickstart

import com.vtence.molecule.Request
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import kickstart.security.Username
import kickstart.security.bindFreshSession
import kickstart.security.username


class HomeControllerTest : JUnit5Minutests {
    class Fixture {
        val view = TestView<Home>()
        val home = HomeController(view)

        val request = Request.get("/")
        val session = request.bindFreshSession()
    }

    @Tests
    fun tests() = rootContext<Fixture> {
        given { Fixture() }

        context("when user signed in") {
            beforeEach {
                session.username = Username("alice@gmail.com")
            }

            test("renders page for currently signed in user") {
                val response = home.render(request)

                assertThat(response)
                    .hasStatus(HttpStatus.OK)
                    .isDone and
                        view renderedWith Home(Username("alice@gmail.com"))
            }
        }
    }
}

