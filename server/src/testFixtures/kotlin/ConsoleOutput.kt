package kickstart

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleOutput(private val stdout: PrintStream) {

    private val output = ByteArrayOutputStream()

    val lines: Collection<String>
        get() = output.toString().split("\n")

    init {
        System.setOut(PrintStream(output))
    }

    fun release() {
        System.setOut(stdout)
    }

    companion object {
        fun capture(): ConsoleOutput {
            return ConsoleOutput(System.out)
        }
    }
}