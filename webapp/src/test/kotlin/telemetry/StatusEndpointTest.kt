package kickstart.telemetry

import com.vtence.molecule.Request
import com.vtence.molecule.http.HttpStatus
import com.vtence.molecule.testing.ResponseAssert.assertThat
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import kotlin.test.Test

class StatusEndpointTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<StatusEndpoint> {
        given { StatusEndpoint() }

        test("responds with ok in plain text") {
            val response = get(Request.get("/status"))

            assertThat(response)
                .isDone
                .hasStatus(HttpStatus.OK)
                .hasContentType("text/plain")
                .hasBodyText("All green.")
        }
    }
}