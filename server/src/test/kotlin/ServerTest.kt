package kickstart

import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.overriding
import com.vtence.molecule.http.HeaderNames
import com.vtence.molecule.testing.http.HttpResponseAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

class ServerTest {
    val config =
        ConfigurationMap("server.quiet" to "false") overriding EnvironmentFile.load("test")

    val server = Server(config[Settings.server.host], config[Settings.server.port])
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(server.uri)

    val console = ConsoleOutput.capture()

    @BeforeEach
    fun `start server`() {
        server.start(config)
    }

    @AfterEach
    fun `stop server`() {
        server.stop()
        console.release()
    }

    @Test
    fun `is alive`() {
        val response = client.send(request.GET(server.resolve("/en/status")))

        assertThat(response).isOK.hasBody("All green.")
    }

    @Test
    fun `sets server header`() {
        val response = client.send(request.GET())
        assertThat(response).hasHeader("Server")
    }

    @Test
    fun `sets date header`() {
        val response = client.send(request.GET())

        assertThat(response).hasHeader("Date")
    }

    @Test
    fun `logs all accesses`() {
        val response = client.send(request.GET(server.resolve("/en/status")))

        assertThat(response).isOK

        assertThat(
            "log output", console.lines, anyElement(
                containsSubstring("\"GET /en/status HTTP/1.1\" 200")
            )
        )
    }

    @Test
    fun `renders static assets`() {
        val response = client.send(request.GET(server.resolve("/favicon.ico")), BodyHandlers.ofByteArray())

        assertThat(response).isOK
            .hasContentType("image/x-icon")
            .isNotChunked
            .hasHeader("Content-Length", "15086")
    }

    @Test
    fun `renders dynamic content as html utf-8 encoded`() {
        val response = client.send(request.GET(server.resolve("/en")))

        assertThat(response).isOK
            .hasContentType("text/html; charset=utf-8")
    }

    @Test
    fun `redirects to localized urls`() {
        val response = client.send(request.GET(server.resolve("/")))

        assertThat(response).hasHeader(HeaderNames.LOCATION, "/en")
    }

    @Test
    fun `responds with localized 404 page when resource is not found`() {
        val response = client.send(request.GET(server.resolve("/fr/404")))

        assertThat(response)
            .hasStatusCode(404)
            .hasBody(containsString("??gar??e, d??sol??"))
    }
}
