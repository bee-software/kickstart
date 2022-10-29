package kickstart

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleOutput(private val stdout: PrintStream) {

    private val output = ByteArrayOutputStream()

    val lines: Collection<String>
        get() = output.toString().split("\n")

    fun capture() {
        System.setOut(PrintStream(output))
    }

    fun release() {
        System.setOut(stdout)
    }

    companion object {
        fun standardOut(): ConsoleOutput {
            return ConsoleOutput(System.out)
        }
    }
}