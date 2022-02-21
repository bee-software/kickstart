package kickstart.telemetry

import kickstart.routes

class TelemetryModule {
    private val status = StatusEndpoint()

    val routes = routes {
        get("/status").to { status.get(it) }
    }
}