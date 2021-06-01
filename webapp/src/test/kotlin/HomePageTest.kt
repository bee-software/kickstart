package kickstart

import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import it.skrape.matchers.toBe
import it.skrape.matchers.toContain
import it.skrape.selects.Doc
import it.skrape.selects.html5.a
import it.skrape.selects.html5.main
import it.skrape.selects.html5.menu
import org.junit.jupiter.api.Test
import java.util.*

class HomePageTest {

    @Test
    fun `is localized`() {
        render(locale = Locale.FRENCH) {
            eachAttribute("lang") should allElements(equalTo("fr"))
        }
    }

    @Test
    fun `has title`() {
        render() {
            titleText shouldBe "Kickstart"
        }
    }

    @Test
    fun `links back home`() {
        render() {
            main {
                menu {
                    "li.active" {
                        a {
                            findFirst {
                                text shouldBe "Home"
                                attribute("href") shouldBe "/en"
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `links to login`() {
        render() {
            main {
                menu {
                    a {
                        withClass = "button"
                        findFirst {
                            text shouldBe "Log in"
                            attribute("href") shouldBe "/en/login"
                        }
                    }
                }
            }
        }
    }

    private fun render(content: Home = Home(), locale: Locale = Locale.getDefault(), expectations: Expectations): Doc {
        return renderView("home", content, locale, expectations)
    }
}