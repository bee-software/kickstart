package kickstart

import com.natpryce.konfig.*
import java.util.logging.Level

object CLI {
    private val options: Array<CommandLineOption> = arrayOf(
        CommandLineOption(Settings.env, long = "env", short = "e"),
        CommandLineOption(Settings.server.host, long = "host"),
        CommandLineOption(Settings.server.port, long = "port", short = "p"),
        CommandLineOption(Settings.quiet, long = "quiet", short = "q")
    )

    fun launch(vararg args: String): Server {
        val config = parse(args)
        println("Starting in ${config[Settings.env]} environment...")

        val server = Server(config[Settings.server.host], config[Settings.server.port])

        if (!config[Settings.quiet]) server.logger = Loggers.toConsole(Level.ALL)
        server.start(config)
        return server
    }

    private fun parse(args: Array<out String>): Configuration {
        val (options, _) = parseArgs(arrayOf(*args), *options) overriding
                ConfigurationMap("env" to "dev", "quiet" to "false")

        return options overriding
                EnvironmentVariables() overriding
                EnvironmentFile.load(options[Settings.env])
    }

    @JvmStatic
    fun main(vararg args: String) {
        val server = launch(*args)
        println("Server started. Access at ${server.uri}")
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Stopped.")
            server.stop()
        })
    }
}