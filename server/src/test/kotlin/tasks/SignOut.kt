package kickstart.tasks

import kickstart.pages.HomePage.ProfileMenu
import kickstart.storytelling.Actor
import kickstart.storytelling.Task

object SignOut : Task {
    override fun invoke(actor: Actor) {
        with(ProfileMenu) {
            actor.does(logout())
        }
//        actor.checks(....isShowingOnScreen())
    }
}

fun signOut() = SignOut
