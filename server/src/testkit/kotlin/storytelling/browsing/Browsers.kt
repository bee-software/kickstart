package kickstart.storytelling.browsing

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

fun defaultBrowser() = chrome(headless = System.getProperty("chrome.headless", "false").toBoolean())

fun chrome(headless: Boolean = false) = ChromeDriver(ChromeOptions().apply {
    setHeadless(headless)
})

