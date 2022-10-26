package kickstart.tasks

import kickstart.Server
import kickstart.storytelling.Actor
import kickstart.storytelling.Task
import kickstart.storytelling.actions.openBrowserAt
import java.net.URI

class Start(private val uri: URI) : Task {
    override fun invoke(actor: Actor) {
        actor.does(openBrowserAt(uri))
    }
}

fun startUsing(application: Server) = Start(application.uri)
