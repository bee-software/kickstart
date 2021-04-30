package kickstart

import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.logging.*


object Loggers {

    fun off() = anonymous(Level.OFF)

    fun anonymous(level: Level = Level.INFO): Logger = Logger.getAnonymousLogger().apply {
        this.useParentHandlers = false
        this.level = level
    }

    fun toConsole(level: Level): Logger {
        val logger = anonymous(level)
        logger.addHandler(PlainHandler.toStandardOutput().apply { setFilter { it.level != Level.SEVERE } })
        logger.addHandler(PlainHandler.toStandardError().apply { setFilter { it.level == Level.SEVERE } })
        return logger
    }
}


class PlainHandler(out: OutputStream) : StreamHandler(out, PlainFormatter()) {

    override fun publish(record: LogRecord) {
        super.publish(record)
        flush()
    }

    companion object {
        fun toStandardOutput() = PlainHandler(System.out)
        fun toStandardError() = PlainHandler(System.err)
    }
}


class PlainFormatter : Formatter() {
    override fun format(record: LogRecord): String {
        var msg = "${record.level}: ${record.message}\n"
        record.thrown?.let { msg += "${record.thrown.stackTrace()}\n" }
        return msg
    }
}


private fun Throwable.stackTrace(): String {
    val stackTrace = StringWriter()
    printStackTrace(PrintWriter(stackTrace))
    return stackTrace.toString()
}