package kickstart

import com.natpryce.konfig.*
import java.util.logging.Level

object CLI {
    private val options: Array<CommandLineOption> = arrayOf(
        CommandLineOption(Settings.env, long = "env", short = "e"),
        CommandLineOption(Settings.server.host, long = "host"),
        CommandLineOption(Settings.server.port, long = "port", short = "p"),
        CommandLineOption(Settings.server.quiet, long = "quiet", short = "q")
    )

    fun launch(vararg args: String): Server {
        val config = parse(args)
        println("Starting in ${config[Settings.env]} environment...")
        val server = Server(config[Settings.server.host], config[Settings.server.port])
        server.start(config)
        return server
    }

    private fun parse(args: Array<out String>): Configuration {
        val (options, _) = parseArgs(arrayOf(*args), *options) overriding ConfigurationMap("env" to "dev")

        return options overriding
                ConfigurationProperties.systemProperties() overriding
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