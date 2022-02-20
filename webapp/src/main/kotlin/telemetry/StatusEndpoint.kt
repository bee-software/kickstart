package kickstart.telemetry

import com.vtence.molecule.Request
import com.vtence.molecule.Response

class StatusEndpoint {

    fun get(request: Request): Response {
        return Response.ok().contentType("text/plain").done("All green.")
    }
}