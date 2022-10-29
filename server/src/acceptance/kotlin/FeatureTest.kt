package kickstart

import kickstart.models.Identities
import kickstart.storytelling.Actor
import kickstart.storytelling.actorAbleTo
import kickstart.storytelling.browsing.BrowseTheWeb
import kickstart.storytelling.browsing.browseTheWebUsing
import kickstart.tasks.signInAs
import kickstart.tasks.signOut
import kickstart.tasks.startUsing
import kickstart.tasks.stopUsing
import kotlin.test.*

class FeatureTest {

    val app = App.start()
    val actor = actorAbleTo(browseTheWebUsing(hisBrowser))

    @BeforeTest
    fun start() {
        actor.does(startUsing(app))
    }

    @AfterTest
    fun stop() {
        actor.does(stopUsing(app))
        app.stop()
    }

    @Test
    fun `example scenario`() {
        actor.wasAbleTo(
            signInAs(Identities.bob),
            signOut()
        )
    }
}
