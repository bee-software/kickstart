package kickstart.i18n

import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.util.*

class I18nTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<I18n> {
        context("with single locale") {
            given { i18n(Locale.ENGLISH) }

            test("supports its default locale") {
                assertThat("default", defaultLocale, equalTo(Locale.ENGLISH))
                assertThat("supported locales", supportedLocales, hasItem(Locale.ENGLISH))
            }
        }

        context("with multiple locales") {
            given { i18n(Locale.ENGLISH, Locale.CANADA_FRENCH, Locale.FRANCE) }

            test("supports additional locales") {
                assertThat(
                    "supported locales", supportedLocales,
                    hasItems(Locale.FRANCE, Locale.CANADA_FRENCH, Locale.ENGLISH)
                )
            }

            test("computes alternative locales") {
                assertThat(
                    "alternatives to english", alternativeTo(Locale.ENGLISH),
                    containsInAnyOrder(Locale.FRANCE, Locale.CANADA_FRENCH)
                )
            }
        }
    }
}
