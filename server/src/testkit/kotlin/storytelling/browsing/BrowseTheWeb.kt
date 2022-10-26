package kickstart.storytelling.browsing

import com.vtence.mario.BrowserDriver
import com.vtence.mario.UnsynchronizedProber
import com.vtence.mario.WebElementDriver
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import java.net.URL

import kickstart.storytelling.Ability
import kickstart.storytelling.Actor
import kickstart.storytelling.Target

class BrowseTheWeb(private val browser: BrowserDriver) : Ability {

    override fun drop() = browser.quit()

    fun navigateTo(location: URL) {
        navigateTo(location.toExternalForm())
    }

    fun navigateTo(location: String) {
        browser.navigate().to(location)
    }

    fun check(target: Target): WebElementDriver {
        return find(target)
    }

    fun find(target: Target): WebElementDriver {
        return target.locate(browser)
    }

    fun pause(millis: Long) {
        browser.pause(millis)
    }

    fun execute(script: String): Any? {
        return (browser.wrappedDriver() as JavascriptExecutor).executeScript(script)
    }
}


private val timeout = System.getProperty("wait.timeout", "5000").toLong()


fun browseTheWebUsing(driver: WebDriver): BrowseTheWeb =
    BrowseTheWeb(BrowserDriver(UnsynchronizedProber(timeout, 50), driver))


fun browsingAs(actor: Actor) =
    actor.ability<BrowseTheWeb>() ?: throw IllegalStateException("cannot browse the web")