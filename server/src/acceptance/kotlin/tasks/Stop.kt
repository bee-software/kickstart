package kickstart.tasks

import kickstart.Server
import kickstart.storytelling.Actor
import kickstart.storytelling.Task

class Stop(private val server: Server) : Task {
    override fun invoke(actor: Actor) {
        actor.leave()
    }
}

fun stopUsing(application: Server) = Stop(application)