package kickstart.pages

import kickstart.storytelling.Target
import kickstart.storytelling.actions.Click
import kickstart.storytelling.facts.isShowingOnScreen

object HomePage {
    private val TOP_MENU = Target.byCssSelector("main menu.top")
    private val LOGIN_BUTTON = Target.byCssSelector("#login a.button").within(TOP_MENU)
    private val LOGOUT_BUTTON = Target.byCssSelector("#logout a.button").within(TOP_MENU)

    fun login() = Click.on(LOGIN_BUTTON)

    fun logout() = Click.on(LOGOUT_BUTTON)

    val isShowing = TOP_MENU.isShowingOnScreen
}
