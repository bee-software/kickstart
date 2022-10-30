package kickstart

import org.openqa.selenium.WebDriver


private val env
    get() = System.getProperty("env.name", "test")


object App {
    fun start(environment: String = env, quiet: Boolean = true) = CLI.launch("-e", environment, "-q", quiet.toString())
}


val hisBrowser: WebDriver
    get() = Browser.from(EnvironmentFile.load(env))