package kickstart

object App {

    fun start(quiet: Boolean = true) = CLI.launch("-e", "acceptance", "-q", quiet.toString())
}