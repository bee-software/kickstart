package kickstart

import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.vtence.molecule.testing.http.HttpResponseAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import java.net.http.HttpRequest

class ServerTest {
    val server = Server("localhost", 19999)

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(server.uri)

    val log = LogCapture()

    @BeforeEach
    fun `start server`() {
        server.logger = log.sink
        server.start(EnvironmentFile.load("test"))
    }

    @AfterEach
    fun `stop server`() {
        server.stop()
    }

    @Test
    fun `is alive`() {
        val response = client.send(request.GET(server.resolve("/status")))

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
        val response = client.send(request.GET(server.resolve("/status")))

        assertThat(response).isOK

        assertThat(
            "log output", log.lines, anyElement(
                containsSubstring("\"GET /status HTTP/1.1\" 200")
            )
        )
    }
}