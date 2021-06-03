package kickstart

import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.equalToIgnoringCase
import it.skrape.matchers.toBeNotPresent
import it.skrape.matchers.toBePresent
import it.skrape.selects.Doc
import it.skrape.selects.html5.a
import it.skrape.selects.html5.main
import it.skrape.selects.html5.menu
import kickstart.security.Username
import java.util.*
import kotlin.test.Test

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
    fun `links to login, when no user is signed in`() {
        render() {
            relaxed = true

            main {
                menu {
                    a {
                        withClass = "button"
                        findFirst {
                            text shouldBe "Log in"
                            attribute("href") shouldBe "/en/login"
                        }
                    }

                    findAll(".avatar.image") { toBeNotPresent }
                }
            }
        }
    }

    @Test
    fun `displays user dropdown menu when signed in, with avatar and signed in email`() {
        render(Home(username = Username("alice@gmail.com"))) {
            relaxed = true

            main {
                menu {
                    findAll("#login a") { toBeNotPresent }

                    ".dropdown.item" {
                        findFirst(".menu .header") {
                            text should equalToIgnoringCase("alice@gmail.com")
                        }

                        findAll(".avatar.image") { toBePresent }
                        // todo
                    }
                }
            }
        }
    }


    private fun render(content: Home = Home(), locale: Locale = Locale.getDefault(), expectations: Expectations): Doc {
        return renderView("home", content, locale, expectations)
    }
}
