package kickstart.tasks

import kickstart.pages.HomePage
import kickstart.storytelling.Actor
import kickstart.storytelling.Task
import kickstart.storytelling.on

object SignOut : Task {
    override fun invoke(actor: Actor) {
        on(HomePage) {
            actor.does(logout())
        }
//        actor.checks(....isShowingOnScreen())
    }
}

fun signOut() = SignOut