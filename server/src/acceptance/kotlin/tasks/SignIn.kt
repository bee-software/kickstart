package kickstart.tasks

import kickstart.models.Identity
import kickstart.pages.HomePage
import kickstart.pages.HomePage.ProfileMenu
import kickstart.pages.LoginPage
import kickstart.storytelling.Actor
import kickstart.storytelling.Task
import kickstart.storytelling.on

class SignIn(private val identity: Identity) : Task {
    override fun invoke(actor: Actor) {
        on(HomePage) {
            actor.attemptsTo(login())
        }
        on(LoginPage) {
            actor.does(
                enterEmail(identity.email),
                enterPassword(identity.password),
                signIn()
            )
        }
        on(HomePage) {
            actor.does(
                openProfileMenu()
            )
            actor.seesThat(ProfileMenu.showsCurrentlySignedInAs(identity.email))
        }
    }
}

fun signInAs(identity: Identity) = SignIn(identity)
