package kickstart.i18n

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test

class CompositeMessagesTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<CompositeMessages> {
        context("when empty") {
            given { compose() }

            test("can't interpolate anything") {
                val composite = compose()

                assertThat("any message", composite.interpolate("message"), absent())
            }
        }

        context("when composed") {
            given { noMessages + Messages { _, _ -> "translation" } }

            test("look up translation in all messages") {
                assertThat("translated message", interpolate("message"), equalTo("translation"))
            }
        }

        context("when overridden") {
            given { Messages { _, _ -> "original" } + Messages { _, _ -> "override" } }

            test("looks ups translations in order of addition") {
                assertThat("original translation", interpolate("message"), equalTo("original"))
            }
        }
    }
}