package kickstart.security

import com.natpryce.hamkrest.containsSubstring
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import it.skrape.selects.html5.form
import kickstart.*
import java.util.*


class LoginPageFixture(login: Login, locale: Locale = Locale.CANADA): PageFixture<Login>("sessions/new", login, locale)


class LoginPageTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<LoginPageFixture> {
        context("initially") {
            given {
                LoginPageFixture(Login())
            }

            test("displays empty login form") {
                render {
                    form("#login") {
                        findFirst {
                            attribute("action") shouldBe "/login"
                            attribute("method") shouldBe "post"
                            className should !containsSubstring("error")

                            findFirst("input[name=email]") {
                                value shouldBe ""
                            }
                            findFirst("input[name=password]") {
                                value shouldBe ""
                            }
                        }
                    }
                }
            }
        }

        context("when form validation fails") {
            given {
                LoginPageFixture(
                    Login("john") + errors.login.email.required("") + errors.login.password.required("")
                )
            }

            test("renders validation errors but keeps posted email") {
                render {
                    form("#login") {
                        findFirst {
                            className should containsSubstring("error")

                            findFirst("input[name=email]") {
                                value shouldBe "john"
                            }

                            findFirst(errorLabelFor("email")) {
                                text shouldBe "Please enter your email"
                            }

                            findFirst("input[name=password]") {
                                value shouldBe ""
                            }

                            findFirst(errorLabelFor("password")) {
                                text shouldBe "Please enter your password"
                            }
                        }
                    }
                }
            }
        }

        context("with invalid credential") {
            given {
                LoginPageFixture(Login.invalid(email = "john"))
            }

            test("reports invalid credentials but keeps posted email") {
                render {
                    form {
                        findFirst {
                            className should containsSubstring("error")
                        }

                        "div.error.message" {
                            findFirst("li") {
                                ownText should containsSubstring("invalid")
                            }
                        }

                        findFirst("input[name=email]") {
                            value shouldBe "john"
                        }
                    }
                }
            }
        }
    }
}