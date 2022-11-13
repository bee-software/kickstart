package kickstart

import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import kickstart.models.Identities
import kickstart.storytelling.Actor
import kickstart.storytelling.actorAbleTo
import kickstart.storytelling.browsing.browseTheWebUsing
import kickstart.tasks.signInAs
import kickstart.tasks.signOut
import kickstart.tasks.startUsing
import kickstart.tasks.stopUsing
import org.junit.platform.commons.annotation.Testable

@Suppress("MemberVisibilityCanBePrivate", "FunctionName")
class FeatureTest : JUnit5Minutests {

    class EndUser {
        val app = App.start()
        val user = actorAbleTo(browseTheWebUsing(hisBrowser))

        fun Actor.startsUsingApp() {
            does(startUsing(app))
        }

        fun Actor.stopsUsingApp() {
            does(stopUsing(app))
            app.stop()
        }
    }

    @Testable
    fun test() = rootContext<EndUser> {
        given { EndUser() }

        beforeEach {
            user.startsUsingApp()
        }

        afterEach {
            user.stopsUsingApp()
        }

        scenario("example") {
            user.wasAbleTo(
                signInAs(Identities.bob),
                //...
                signOut()
            )
        }
    }
}
