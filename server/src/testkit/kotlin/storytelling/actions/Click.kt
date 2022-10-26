package kickstart.storytelling.actions

import kickstart.storytelling.Target
import kickstart.storytelling.Actor
import kickstart.storytelling.browsing.browsingAs

class Click(private val target: Target): Action {

    override fun invoke(actor: Actor) {
        browsingAs(actor).find(target).click()
    }
}


fun clickOn(target: Target) = Click(target)
