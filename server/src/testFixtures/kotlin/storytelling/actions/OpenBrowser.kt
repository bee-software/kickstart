package kickstart.storytelling.actions

import kickstart.storytelling.Actor
import kickstart.storytelling.Info
import kickstart.storytelling.browsing.browsingAs
import java.net.URI
import java.net.URL

class OpenBrowser(private val start: URL) : Action {

    override fun invoke(actor: Actor) {
        browsingAs(actor).navigateTo(start)
        actor.remembers(startUrl, start)
    }

    companion object {
        val startUrl = Info.create<URL>()
    }
}

fun openBrowserAt(uri: URI) = openBrowserAt(uri.toURL())

fun openBrowserAt(url: URL) = OpenBrowser(url)
