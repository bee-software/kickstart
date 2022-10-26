package kickstart.storytelling

import com.vtence.mario.WebElementDriver
import com.vtence.mario.WebElementLocator
import org.openqa.selenium.By

sealed class Target {

    infix fun within(parent: Target) = Within(parent, this)

    abstract fun locate(locator: WebElementLocator): WebElementDriver
}

class TargetLocatedBy(private val criteria: By) : Target() {

    override fun locate(locator: WebElementLocator): WebElementDriver {
        return locator.element(criteria)
    }
}

class Within(private val parent: Target, private val child: Target) : Target() {

    override fun locate(locator: WebElementLocator): WebElementDriver {
        return child.locate(parent.locate(locator))
    }
}


fun byId(id: String) = located(By.id(id))

fun byCssSelector(selector: String) = located(By.cssSelector(selector))

fun byXPath(xpath: String) = located(By.xpath(xpath))

fun byName(name: String) = located(By.name(name))

fun byClassName(className: String) = located(By.className(className))

fun byLinkText(text: String) = located(By.linkText(text))

fun byTagName(name: String) = located(By.tagName(name))

fun located(criteria: By) = TargetLocatedBy(criteria)

