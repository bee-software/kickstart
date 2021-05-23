package kickstart.pages

import kickstart.storytelling.Target
import kickstart.storytelling.actions.*

object LoginPage {
    private val LOGIN_FORM = Target.byCssSelector("form#login")
    private val USERNAME_INPUT = Target.byName("username").within(LOGIN_FORM)
    private val PASSWORD_INPUT = Target.byName("password").within(LOGIN_FORM)
    private val LOGIN_BUTTON = Target.byTagName("button").within(LOGIN_FORM)

    fun enterUsername(username: String): Action {
        return enterText(username).into(USERNAME_INPUT)
    }

    fun enterPassword(password: String): Action {
        return enterText(password).into(PASSWORD_INPUT)
    }

    fun signIn(): Action {
        return clickOn(LOGIN_BUTTON)
    }
}
