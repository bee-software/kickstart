package kickstart.pages

import kickstart.storytelling.Target
import kickstart.storytelling.actions.Action
import kickstart.storytelling.actions.clickOn
import kickstart.storytelling.actions.enterText

object LoginPage {
    private val login_form = Target.byCssSelector("form#login")
    private val email_input = Target.byName("email").within(login_form)
    private val password_input = Target.byName("password").within(login_form)
    private val login_button = Target.byTagName("button").within(login_form)

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
