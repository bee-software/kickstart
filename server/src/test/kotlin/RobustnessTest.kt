package kickstart

import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.overriding
import com.vtence.molecule.testing.http.HttpResponseAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.net.http.HttpClient
import java.net.http.HttpRequest
import kotlin.test.Test

class RobustnessTest {
    val config = ConfigurationMap("db.password" to "wrong secret") overriding
            EnvironmentFile.load("test")

    val server = Server(config[Settings.server.host], config[Settings.server.port])

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(server.uri)

    @BeforeEach
    fun `turn off logging`() {
        Loggers.off()
    }

    @BeforeEach
    fun `start server`() {
        server.start(config)
    }

    @AfterEach
    fun `stop server`() {
        server.stop()
    }

    @Test
    fun `renders 500 in case of internal error`() {
        val response = client.send(request.GET(server.resolve("/")))
        assertThat(response).hasStatusCode(500)
    }
}