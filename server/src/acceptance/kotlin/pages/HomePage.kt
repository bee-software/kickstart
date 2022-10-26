package kickstart.pages

import com.natpryce.hamkrest.equalToIgnoringCase
import kickstart.hasText
import kickstart.storytelling.Fact
import kickstart.storytelling.Target
import kickstart.storytelling.actions.Click
import kickstart.storytelling.actions.clickOn
import kickstart.storytelling.browsing.browsingAs
import kickstart.storytelling.byCssSelector
import kickstart.storytelling.facts.isShowingOnScreen

object HomePage {
    private val top_menu = byCssSelector("main menu.top")
    private val profile_menu = byCssSelector(".icon.dropdown.item").within(top_menu)
    private val login_button = byCssSelector("#login .button").within(top_menu)
    private val logout_button = byCssSelector("#logout .button").within(top_menu)

    fun login(): Click {
        return clickOn(login_button)
    }

    fun logout(): Click {
        return clickOn(logout_button)
    }

    fun showsLoginButton(): Fact {
        return login_button.isShowingOnScreen
    }

    fun openProfileMenu(): Click {
        return clickOn(profile_menu)
    }

    object ProfileMenu {
        private val header = byCssSelector(".header").within(profile_menu)

        fun showsCurrentlySignedInAs(email: String): Fact = { actor ->
            browsingAs(actor).check(header).hasText(equalToIgnoringCase(email))
        }
    }
}
