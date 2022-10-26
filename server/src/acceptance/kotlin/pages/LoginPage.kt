package kickstart.pages

import kickstart.storytelling.Target
import kickstart.storytelling.actions.Action
import kickstart.storytelling.actions.clickOn
import kickstart.storytelling.actions.enterText
import kickstart.storytelling.byCssSelector
import kickstart.storytelling.byName
import kickstart.storytelling.byTagName

object LoginPage {
    private val login_form = byCssSelector("form#login")
    private val email_input = byName("email").within(login_form)
    private val password_input = byName("password").within(login_form)
    private val login_button = byTagName("button").within(login_form)

    fun enterEmail(email: String): Action {
        return enterText(email).into(email_input)
    }

    fun enterPassword(password: String): Action {
        return enterText(password).into(password_input)
    }

    fun signIn(): Action {
        return clickOn(login_button)
    }
}
