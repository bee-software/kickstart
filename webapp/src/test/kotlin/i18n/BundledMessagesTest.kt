package i18n

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import kickstart.i18n.BundledMessages
import kickstart.i18n.loadBundle
import java.util.*

class BundledMessagesTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<BundledMessages> {
        given { BundledMessages.rootedAt("test/i18n") }

        test("loads translations in specified locale") {
            val translations = loadBundle("defaults", Locale.FRENCH)

            assertThat(
                "translated value", translations.interpolate("app.value"),
                equalTo("valeur par défaut")
            )
        }

        test("composes view translations") {
            val translations = loadBundle("views", "model/page", Locale.ENGLISH)

            assertThat(
                "page value", translations.interpolate("page.value"),
                equalTo("page value")
            )
            assertThat(
                "model value", translations.interpolate("model.value"),
                equalTo("model default value")
            )
            assertThat(
                "default value", translations.interpolate("views.value"),
                equalTo("views default value")
            )
        }

        test("looks up translation keys, starting with most specific") {
            fun lookupValueIn(bundleName: String): String? {
                val translations = loadBundle("views", bundleName, Locale.ENGLISH)
                return translations.interpolate("value")
            }

            assertThat("page value", lookupValueIn("model/page"), equalTo("page value"))
            assertThat(
                "model default value",
                lookupValueIn("model/other"),
                equalTo("model default value")
            )
            assertThat("views default value", lookupValueIn("other"), equalTo("views default value"))
        }

        test("ignores missing bundles") {
            val translations = loadBundle("missing", Locale.ENGLISH)

            assertThat(translations.interpolate("key"), absent())
        }

        test("provides search locations") {
            val translations = loadBundle("views", "model/page", Locale.ENGLISH)

            val locations = translations.searchPath("views.value")
                .map { it.description }

            assertThat(
                locations,
                allOf(
                    hasElement("bundle test/i18n/views/model/page in en"),
                    hasElement("bundle test/i18n/views/model/defaults in en"),
                    hasElement("bundle test/i18n/views/defaults in en"))
            )
        }
    }
}