package i18n

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kickstart.i18n.BundledMessages
import kickstart.i18n.loadBundle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Paths
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
        val translations = bundles.at(Paths.get("views")).loadBundle("model/page", Locale.ENGLISH)

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

        assertThrows<MissingResourceException> {  translations.interpolate("key") }
    }

    private fun lookupValueIn(bundleName: String): String {
        val translations = bundles.at(Paths.get("views")).loadBundle(bundleName, Locale.ENGLISH)
        return translations.interpolate("value")
    }
}