package kickstart

import com.natpryce.konfig.*
import kickstart.BrowserSettings.selenium
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.lang.IllegalArgumentException
import java.net.MalformedURLException
import java.net.URL


private val urlType = propertyType(parser<URL, MalformedURLException>(::URL))


object BrowserSettings {

    object selenium : PropertyGroup() {
        val driver by stringType

        object chrome : PropertyGroup() {
            val headless by booleanType
        }

        object remote : PropertyGroup() {
            val location by urlType
        }

        // todo
        object capabilities : PropertyGroup()
    }
}


object Browser {

    fun from(options: Configuration) = when (val driver = options[selenium.driver]) {
        "chrome" -> chrome(options)
        "remote" -> remote(options)
        else -> throw IllegalArgumentException("Unsupported selenium driver type `$driver`")
    }

    fun chrome(options: Configuration): WebDriver = ChromeDriver(ChromeOptions().apply {
        setHeadless(options[selenium.chrome.headless])
    })

    fun remote(options: Configuration): WebDriver {
        return RemoteWebDriver(options[selenium.remote.location], ChromeOptions(), true)
    }
}
