package kickstart.tasks

import kickstart.models.Identity
import kickstart.pages.HomePage
import kickstart.pages.LoginPage
import kickstart.storytelling.Actor
import kickstart.storytelling.Task
import kickstart.storytelling.on

class SignIn(private val identity: Identity) : Task {
    override fun invoke(actor: Actor) {
        on(HomePage) {
            actor.does(login())
        }
        on(LoginPage) {
            actor.does(
                enterUsername(identity.username),
                enterPassword(identity.password),
                signIn()
            )
        }
        actor.checks(HomePage.isShowing)
    }

    companion object {
        fun `as`(identity: Identity) = SignIn(identity)
    }
}

fun signInAs(identity: Identity) = SignIn.`as`(identity)
