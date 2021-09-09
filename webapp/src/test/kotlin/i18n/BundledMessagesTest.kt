package i18n

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import kickstart.i18n.BundledMessages
import kickstart.i18n.loadBundle
import kotlin.test.Test
import java.util.*

class BundledMessagesTest {

    val bundles = BundledMessages.rootedAt("test/i18n")

    @Test
    fun `loads translations in specified locale`() {
        val translations = bundles.loadBundle("defaults", Locale.FRENCH)

        assertThat(
            "translated value", translations.interpolate("app.value"),
            equalTo("valeur par défaut")
        )
    }

    @Test
    fun `composes view translations`() {
        val translations = bundles.loadBundle("views", "model/page", Locale.ENGLISH)

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

    @Test
    fun `looks up translation keys, starting with most specific`() {
        fun lookupValueIn(bundleName: String): String? {
            val translations = bundles.loadBundle("views", bundleName, Locale.ENGLISH)
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

    @Test
    fun `ignores missing bundles`() {
        val translations = bundles.loadBundle("missing", Locale.ENGLISH)

        assertThat(translations.interpolate("key"), absent())
    }

    @Test
    fun `provides search locations`() {
        val translations = bundles.loadBundle("views", "model/page", Locale.ENGLISH)

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