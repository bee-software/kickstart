package kickstart

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

val hisBrowser get() = chrome(headless = System.getProperty("chrome.headless", "false").toBoolean())

private fun chrome(headless: Boolean = false) = ChromeDriver(ChromeOptions().apply {
    setHeadless(headless)
})

