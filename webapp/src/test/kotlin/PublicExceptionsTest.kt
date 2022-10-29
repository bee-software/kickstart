package kickstart

import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResourceLocator
import com.vtence.molecule.testing.ResponseAssert.assertThat
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import kickstart.i18n.locale
import org.hamcrest.Matchers.containsString
import java.nio.charset.StandardCharsets
import java.util.*


private class ExceptionPageFixture {
    val exceptions = PublicExceptions(ResourceLocator.locateOnClasspath("test/pages").toPath())

    val request: Request = Request.get("/")

    fun handle(request: Request, next: Application): Response {
        return exceptions.then(next).handle(request)
    }
}

class PublicExceptionsTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<ExceptionPageFixture> {
        given { ExceptionPageFixture() }

        test("renders html exception page, named after status code") {
            val response = handle(request) { Response.of(HttpStatus.BAD_GATEWAY).done() }

            assertThat(response)
                .hasStatus(HttpStatus.BAD_GATEWAY)
                .hasContentType("text/html; charset=utf-8")
                .hasHeader("Content-Length", response.body().size(StandardCharsets.UTF_8).toString())
                .hasBodyText(containsString("Bad Gateway (502)"))
        }

        test("leaves response unchanged if no matching error page can be found") {
            val response = handle(request) { Response.ok().done("All good") }

            assertThat(response).hasStatus(HttpStatus.OK).hasBodyText("All good")
        }

        context("when locale supported") {
            beforeEach {
                request.locale = Locale.CANADA_FRENCH
            }

            test("renders localized version of page") {
                val response = handle(request) {
                    Response.of(HttpStatus.BAD_GATEWAY).done()
                }

                assertThat(response).hasBodyText(containsString("Passerelle en panne"))
            }
        }

        context("when locale not supported") {
            beforeEach {
                request.locale = Locale.JAPANESE
            }

            test("falls back to non localized page otherwise") {
                val response = handle(request) {
                    Response.of(HttpStatus.BAD_GATEWAY).done()
                }

                assertThat(response).hasBodyText(containsString("Bad Gateway"))
            }
        }
    }
}
