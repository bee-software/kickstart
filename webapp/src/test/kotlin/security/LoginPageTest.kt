package kickstart.security

import com.natpryce.hamkrest.containsSubstring
import it.skrape.selects.Doc
import it.skrape.selects.html5.form
import kickstart.*
import kotlin.test.Test
import java.util.*

class LoginPageTest {

    @Test
    fun `displays login form, initially empty`() {
        render(Login.empty) {
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

    @Test
    fun `reports invalid credentials, keeping posted email`() {
        render(Login.invalid(email = "john")) {
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

    @Test
    fun `renders errors when form is invalid, keeping posted email`() {
        render(
            Login("john")
                    + errors.login.email.required("")
                    + errors.login.password.required("")
        ) {
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

private fun render(model: Login, locale: Locale = Locale.getDefault(), expectations: Expectations): Doc {
    return renderView("sessions/new", model, locale, expectations)
}
