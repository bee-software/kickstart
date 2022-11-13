package kickstart.pages

import com.natpryce.hamkrest.equalToIgnoringCase
import kickstart.hasText
import kickstart.storytelling.Fact
import kickstart.storytelling.actions.Click
import kickstart.storytelling.actions.clickOn
import kickstart.storytelling.browsing.browsingAs
import kickstart.storytelling.byCssSelector
import kickstart.storytelling.facts.isShowingOnScreen

object HomePage {
    private val topMenu = byCssSelector("main menu.top")
    private val profileMenu = byCssSelector(".icon.dropdown.item").within(topMenu)
    private val loginButton = byCssSelector("#login .button").within(topMenu)
    private val logoutButton = byCssSelector("#logout .button").within(topMenu)

    fun login(): Click {
        return clickOn(loginButton)
    }

    fun logout(): Click {
        return clickOn(logoutButton)
    }

    fun showsLoginButton(): Fact {
        return loginButton.isShowingOnScreen
    }

    fun openProfileMenu(): Click {
        return clickOn(profileMenu)
    }

    object ProfileMenu {
        private val header = byCssSelector(".header").within(profileMenu)

        fun showsCurrentlySignedInAs(email: String): Fact = { actor ->
            browsingAs(actor).check(header) hasText equalToIgnoringCase(email)
        }
    }
}
