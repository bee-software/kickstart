package kickstart

import com.vtence.molecule.Request
import com.vtence.molecule.Response
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResourceLocator
import com.vtence.molecule.testing.ResponseAssert.assertThat
import kickstart.i18n.locale
import org.hamcrest.Matchers.containsString
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.test.Test


class PublicExceptionsTest {
    val exceptions = PublicExceptions(ResourceLocator.locateOnClasspath("test/pages").toPath())

    val request: Request = Request.get("/")

    @Test
    fun `renders html exception page, named after status code`() {
        val response = exceptions.then { _ -> Response.of(HttpStatus.BAD_GATEWAY).done() }
            .handle(request)

        assertThat(response)
            .hasStatus(HttpStatus.BAD_GATEWAY)
            .hasContentType("text/html; charset=utf-8")
            .hasHeader("Content-Length", response.body().size(StandardCharsets.UTF_8).toString())
            .hasBodyText(containsString("Bad Gateway (502)"))
    }

    @Test
    fun `renders localized version of page if possible`() {
        request.locale = Locale.CANADA_FRENCH
        val response = exceptions.then { _ -> Response.of(HttpStatus.BAD_GATEWAY).done() }
            .handle(request)

        assertThat(response).hasBodyText(containsString("Passerelle en panne"))
    }

    @Test
    fun `falls back to non localized page otherwise`() {
        request.locale = Locale.JAPANESE
        val response = exceptions.then { _ -> Response.of(HttpStatus.BAD_GATEWAY).done() }
            .handle(request)

        assertThat(response).hasBodyText(containsString("Bad Gateway"))
    }

    @Test
    fun `leaves response unchanged if no matching error page can be found`() {
        val response = exceptions.then { _  -> Response.ok().done("All good") }
            .handle(request)

        assertThat(response).hasStatus(HttpStatus.OK).hasBodyText("All good")
    }
}
