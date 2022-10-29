package kickstart

import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.overriding
import com.vtence.molecule.http.HeaderNames
import com.vtence.molecule.testing.http.HttpResponseAssert.assertThat
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import org.hamcrest.Matchers.containsString
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers


class ServerFixture(private val config: Configuration) {
    val server = Server(config[Settings.server.host], config[Settings.server.port])
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(server.uri)

    val console = ConsoleOutput.capture()

    fun start() {
        server.start(config)
    }

    fun stop() {
        server.stop()
        console.release()
    }

    fun get(path: String): HttpResponse<String> {
        return get(path, BodyHandlers.ofString())
    }

    fun <T> get(path: String, handler: HttpResponse.BodyHandler<T>): HttpResponse<T> {
        return client.send(request.uri(server.resolve(path)).GET().build(), handler)
    }
}

class ServerTest : JUnit5Minutests {
    fun `configuration tests`() = rootContext<ServerFixture> {
        given {
            ServerFixture(ConfigurationMap("server.quiet" to "false") overriding EnvironmentFile.load("test"))
        }

        beforeEach {
            start()
        }

        afterEach {
            stop()
        }

        test("is alive") {
            val response = get("/en/status")

            assertThat(response).isOK.hasBody("All green.")
        }

        test("sets server header") {
            val response = get("/")
            assertThat(response).hasHeader("Server")
        }

        test("sets date header") {
            val response = get("/")

            assertThat(response).hasHeader("Date")
        }

        test("logs all accesses") {
            val response = get("/en/status")

            assertThat(response).isOK

            assertThat(
                "log output", console.lines, anyElement(
                    containsSubstring("\"GET /en/status HTTP/1.1\" 200")
                )
            )
        }

        test("renders static assets") {
            val response = get("/favicon.ico", BodyHandlers.ofByteArray())

            assertThat(response).isOK
                .hasContentType("image/x-icon")
                .isNotChunked
                .hasHeader("Content-Length", "15086")
        }

        test("renders dynamic content as html utf-8 encoded") {
            val response = get("/en")

            assertThat(response).isOK
                .hasContentType("text/html; charset=utf-8")
        }

        test("redirects to localized urls") {
            val response = get("/")

            assertThat(response).hasHeader(HeaderNames.LOCATION, "/en")
        }
    }

    fun `robustness tests`() = rootContext<ServerFixture> {
        given {
            ServerFixture(
                ConfigurationMap("db.password" to "wrong secret") overriding EnvironmentFile.load("test")
            )
        }

        beforeEach {
            start()
        }

        afterEach {
            stop()
        }

        test("renders 500 in case of internal error") {
            val response = get("/")
            assertThat(response).hasStatusCode(500)
        }
    }
}
