package kickstart

import it.skrape.matchers.toBe
import it.skrape.matchers.toContain
import it.skrape.selects.Doc
import it.skrape.selects.html5.a
import it.skrape.selects.html5.main
import it.skrape.selects.html5.menu
import kickstart.i18n.Translations
import org.junit.jupiter.api.Test
import java.util.*

class HomePageTest {

    @Test
    fun `is localized`() {
        render(Home(Translations(Locale.ENGLISH))) {
            eachAttribute("lang") toContain "en"
        }
    }

    @Test
    fun `has title`() {
        render() {
            titleText toBe "Kickstart"
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
                                text toBe "Home"
                                attribute("href") toBe "/en"
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
                            text toBe "Log in"
                            attribute("href") toBe "/en/login"
                        }
                    }
                }
            }
        }
    }

    private fun render(content: Home = Home(), expectations: Expectations): Doc {
        return renderView("home", content, expectations)
    }
}