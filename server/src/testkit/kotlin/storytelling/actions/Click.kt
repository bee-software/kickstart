package kickstart.storytelling.actions

import kickstart.storytelling.Target
import kickstart.storytelling.Actor
import kickstart.storytelling.browsing.browsingAs

class Click(private val target: Target): Action {

    override fun invoke(actor: Actor) = browsingAs(actor).find(target).click()

    companion object {
        fun on(target: Target) = Click(target)
    }
}

fun clickOn(target: Target) = Click.on(target)
