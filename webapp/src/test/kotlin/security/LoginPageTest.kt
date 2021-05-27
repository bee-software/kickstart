package kickstart.security

import it.skrape.matchers.toBe
import it.skrape.matchers.toContain
import it.skrape.matchers.toNotContain
import it.skrape.selects.Doc
import it.skrape.selects.html5.form
import it.skrape.selects.html5.input
import kickstart.Expectations
import kickstart.renderView
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

                    input("[name=username]") {
                        findFirst {
                            attribute("value") toBe ""
                        }
                    }
                    input("[name=password]") {
                        findFirst {
                            attribute("value") toBe ""
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `displays form error list, keeping posted username`() {
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

                input("[name=username]") {
                    findFirst {
                        attribute("value") toBe "john"
                    }
                }
            }
        }
    }


    private fun render(model: Login, locale: Locale = Locale.getDefault(), expectations: Expectations): Doc {
        return renderView("sessions/new", model, locale, expectations)
    }
}