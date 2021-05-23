package kickstart.security

import it.skrape.matchers.toBe
import it.skrape.selects.Doc
import it.skrape.selects.html5.form
import it.skrape.selects.html5.input
import kickstart.Expectations
import kickstart.renderView
import org.junit.jupiter.api.Test

class LoginPageTest {

    @Test
    fun `displays login form, initially empty`() {
        render(Login()) {
            form("#login") {
                findFirst {
                    attribute("action") toBe "/login"
                    attribute("method") toBe "post"

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

    private fun render(model: Login, expectations: Expectations): Doc {
        return renderView("sessions/new", model, expectations)
    }
}