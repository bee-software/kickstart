package kickstart.pages

import kickstart.storytelling.actions.Action
import kickstart.storytelling.actions.clickOn
import kickstart.storytelling.actions.enterText
import kickstart.storytelling.byCssSelector
import kickstart.storytelling.byName
import kickstart.storytelling.byTagName

object LoginPage {
    private val loginForm = byCssSelector("form#login")
    private val emailInput = byName("email").within(loginForm)
    private val passwordInput = byName("password").within(loginForm)
    private val loginButton = byTagName("button").within(loginForm)

    fun enterEmail(email: String): Action {
        return enterText(email) into emailInput
    }

    fun enterPassword(password: String): Action {
        return enterText(password) into passwordInput
    }

    fun signIn(): Action {
        return clickOn(loginButton)
    }
}
