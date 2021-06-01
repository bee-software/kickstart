package kickstart.security

import it.skrape.matchers.toBe
import it.skrape.matchers.toContain
import it.skrape.matchers.toNotContain
import it.skrape.selects.Doc
import it.skrape.selects.html5.form
import kickstart.Expectations
import kickstart.errorLabelFor
import kickstart.renderView
import kickstart.value
import org.junit.jupiter.api.Test
import java.util.*

class LoginPageTest {

    @Test
    fun `displays login form, initially empty`() {
        render(Login.empty) {
            form("#login") {
                findFirst {
                    attribute("action") toBe "/login"
                    attribute("method") toBe "post"
                    className toNotContain "error"

                    findFirst("input[name=username]") {
                        value toBe ""
                    }
                    findFirst("input[name=password]") {
                        value toBe ""
                    }
                }
            }
        }
    }

    @Test
    fun `reports invalid credentials, keeping posted username`() {
        render(Login.invalid(username = "john")) {
            form {
                findFirst {
                    className toContain "error"
                }

                "div.error.message" {
                    findFirst("li") {
                        ownText toContain "invalid"
                    }
                }

                findFirst("input[name=username]") {
                    value toBe "john"
                }
            }
        }
    }

    @Test
    fun `renders errors when form is invalid, keeping posted username`() {
        render(
            Login("john")
                    + errors.login.username.required("")
                    + errors.login.password.required("")
        ) {
            form("#login") {
                findFirst {
                    className toContain "error"

                    findFirst("input[name=username]") {
                        value toBe "john"
                    }

                    findFirst(errorLabelFor("username")) {
                        text toBe "Please enter your username"
                    }

                    findFirst("input[name=password]") {
                        value toBe ""
                    }

                    findFirst(errorLabelFor("password")) {
                        text toBe "Please enter your password"
                    }
                }
            }
        }
    }
}

private fun render(model: Login, locale: Locale = Locale.getDefault(), expectations: Expectations): Doc {
    return renderView("sessions/new", model, locale, expectations)
}
