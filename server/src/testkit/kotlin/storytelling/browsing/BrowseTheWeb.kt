package kickstart.storytelling.browsing

import com.vtence.mario.BrowserDriver
import com.vtence.mario.UnsynchronizedProber
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import java.net.URL

import kickstart.storytelling.Ability
import kickstart.storytelling.Actor
import kickstart.storytelling.Target

class BrowseTheWeb(private val browser: BrowserDriver) : Ability {

    override fun drop() = browser.quit()

    fun navigateTo(location: URL) = navigateTo(location.toExternalForm())

    fun navigateTo(location: String) = browser.navigate().to(location)

    fun check(target: Target) = find(target)

    fun find(target: Target) = target.locate(browser)

    fun pause(millis: Long) = browser.pause(millis)

    fun execute(script: String) = (browser.wrappedDriver() as JavascriptExecutor).executeScript(script)

    companion object {
        private val timeout = System.getProperty("wait.timeout", "5000").toLong()

        fun using(driver: WebDriver): BrowseTheWeb {
            return BrowseTheWeb(BrowserDriver(UnsynchronizedProber(timeout, 50), driver))
        }

        fun `as`(actor: Actor): BrowseTheWeb = actor.abilityTo(BrowseTheWeb::class)
            ?: throw IllegalStateException("cannot browse the web")
    }
}

fun browsingAs(actor: Actor) = BrowseTheWeb.`as`(actor)