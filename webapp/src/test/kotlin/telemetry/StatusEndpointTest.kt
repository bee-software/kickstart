package kickstart.telemetry

import com.vtence.molecule.Request
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import kotlin.test.Test

class StatusEndpointTest {
    val endpoint = StatusEndpoint()

    @Test
    fun `responds with ok in plain text`() {
        val response = endpoint.get(Request.get("/status"))

        assertThat(response)
            .isDone
            .hasStatus(HttpStatus.OK)
            .hasContentType("text/plain")
            .hasBodyText("All green.")
    }
}