package kickstart

import kickstart.models.Users
import kickstart.storytelling.Actor
import kickstart.storytelling.browsing.BrowseTheWeb
import kickstart.tasks.signInAs
import kickstart.tasks.startUsing
import kickstart.tasks.stopUsing
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FeatureTest {

    val application = CLI.launch("-e", "test", "-q", "true")

    val actor = Actor.ableTo(BrowseTheWeb.withHisBrowser())

    @BeforeEach
    fun start() {
        actor.does(startUsing(application))
    }

    @AfterEach
    fun stop() {
        actor.does(stopUsing(application))
    }

    @Test
    fun `example scenario`() {
        actor.wasAbleTo(
            signInAs(Users.bob),
//            signOut()
        )
    }
}