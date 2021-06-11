package kickstart

import kickstart.models.Users
import kickstart.storytelling.Actor
import kickstart.storytelling.browsing.BrowseTheWeb
import kickstart.tasks.signInAs
import kickstart.tasks.signOut
import kickstart.tasks.startUsing
import kickstart.tasks.stopUsing
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class FeatureTest {

    val application = CLI.launch("-e", "test", "-q", "true")

    val actor = Actor.ableTo(BrowseTheWeb.withHisBrowser())

    @BeforeTest
    fun start() {
        actor.does(startUsing(application))
    }

    @AfterTest
    fun stop() {
        actor.does(stopUsing(application))
    }

    @Test
    fun `example scenario`() {
        actor.wasAbleTo(
            signInAs(Users.bob),
            signOut()
        )
    }
}
