package kickstart

import java.io.ByteArrayOutputStream
import java.util.logging.Logger

class LogCapture {

    private val output = ByteArrayOutputStream()

    val sink: Logger = Loggers.anonymous().apply {
        addHandler(PlainHandler(output))
    }

    val lines: Collection<String>
        get() = output.toString().split("\n")
}