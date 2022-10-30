package kickstart

import com.natpryce.hamkrest.equalToIgnoringCase
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import it.skrape.matchers.toBeNotPresent
import it.skrape.selects.html5.a
import it.skrape.selects.html5.main
import it.skrape.selects.html5.menu
import kickstart.security.Username
import java.util.*


class HomePageFixture(content: Home = Home(), locale: Locale = Locale.getDefault()) : PageFixture<Home>("home", content, locale)


fun ContextBuilder<HomePageFixture>.showsTopMenu() {
    context("shows top menu") {

        test("has page title") {
            render {
                titleText shouldBe "Kickstart"
            }
        }

        test("links back home") {
            render {
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
    }
}


class HomePageTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<HomePageFixture> {
        context("when not signed in") {
            given { HomePageFixture() }

            showsTopMenu()

            test("links to login") {
                render(relaxed = true) {
                    main {
                        menu {
                            findFirst("#login a") {
                                text shouldBe "Log in"
                                attribute("href") shouldBe "/en/login"
                            }

                            findAll(".avatar.image") { toBeNotPresent }
                        }
                    }
                }
            }
        }

        context("when signed in") {
            given { HomePageFixture(Home(username = Username("alice@gmail.com"))) }

            showsTopMenu()

            test("displays user avatar and signed in email in profile dropdown menu") {
                render {
                    main {
                        menu {
                            ".dropdown.item" {
                                findFirst(".menu .header") {
                                    text should equalToIgnoringCase("alice@gmail.com")
                                }

                                findFirst(".avatar img") {
                                    attribute("src") shouldBe "//www.gravatar.com/avatar/0ce273d3249291c620af81403b14b3c1?d=mp&s=32"
                                }
                            }
                        }
                    }
                }
            }


            test("offers logout option") {
                render(relaxed = true) {
                    main {
                        menu {
                            findAll("#login a") { toBeNotPresent }

                            findFirst("form#logout") {
                                attribute("action") shouldBe "/logout"
                                attribute("method") shouldBe "post"

                                findFirst("input[name=_method]") {
                                    value should equalToIgnoringCase("delete")
                                }
                                findFirst("button") {
                                    text shouldBe "Log out"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
