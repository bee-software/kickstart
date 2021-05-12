package kickstart

import Expectations
import it.skrape.matchers.toBe
import it.skrape.matchers.toContain
import it.skrape.selects.Doc
import it.skrape.selects.html5.a
import it.skrape.selects.html5.main
import it.skrape.selects.html5.menu
import kickstart.i18n.LocalizedContent
import org.junit.jupiter.api.Test
import renderView

class HomePageTest {

    @Test
    fun `is localized`() {
        render(LocalizedContent()) {
            eachAttribute("lang") toContain "en"
        }
    }

    @Test
    fun `has title`() {
        render(LocalizedContent()) {
            titleText toBe "Homepage - Kickstart"
        }
    }

    @Test
    fun `links back home`() {
        render(LocalizedContent()) {
            main {
                menu {
                    "li.active" {
                        a {
                            findFirst {
                                text toBe "Home"
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `links to login`() {
        render(LocalizedContent()) {
            main {
                menu {
                    a {
                        withClass = "button"
                        findFirst {
                            text toBe "Log in"
                        }
                    }
                }
            }
        }
    }

    private fun render(content: LocalizedContent, expectations: Expectations): Doc {
        return renderView("home", content, expectations)
    }
}