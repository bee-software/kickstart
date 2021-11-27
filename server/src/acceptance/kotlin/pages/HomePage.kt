package kickstart.pages

import com.natpryce.hamkrest.equalToIgnoringCase
import kickstart.hasText
import kickstart.storytelling.Fact
import kickstart.storytelling.Target
import kickstart.storytelling.actions.clickOn
import kickstart.storytelling.browsing.browsingAs
import kickstart.storytelling.facts.isShowingOnScreen

object HomePage {
    private val top_menu = Target.byCssSelector("main menu.top")
    private val profile_menu = Target.byCssSelector(".icon.dropdown.item").within(top_menu)
    private val login_button = Target.byCssSelector("#login .button").within(top_menu)
    private val logout_button = Target.byCssSelector("#logout .button").within(top_menu)

    fun login() = clickOn(login_button)

    fun logout() = clickOn(logout_button)

    fun showsLoginButton() = login_button.isShowingOnScreen

    fun openProfileMenu() = clickOn(profile_menu)

    object ProfileMenu {
        private val header = Target.byCssSelector(".header").within(profile_menu)

        fun showsCurrentlySignedInAs(email: String): Fact = { actor ->
            browsingAs(actor).check(header).hasText(equalToIgnoringCase(email))
        }

    }
}
