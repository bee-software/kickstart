package kickstart.i18n

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.util.*
import kotlin.test.Test

class I18nTest {
    val i18n = i18n(Locale.ENGLISH, Locale.CANADA_FRENCH, Locale.FRANCE)

    @Test
    fun `supports its default locale`() {
        assertThat("default", i18n.defaultLocale, equalTo(Locale.ENGLISH))
        assertThat("supported locales", i18n.supportedLocales, hasItem(Locale.ENGLISH))
    }

    @Test
    fun `supports additional locales`() {
        assertThat(
            "supported locales", i18n.supportedLocales,
            hasItems(Locale.FRANCE, Locale.CANADA_FRENCH, Locale.ENGLISH)
        )
    }

    @Test
    fun `computes alternative locales`() {
        assertThat(
            "alternatives to english", i18n.alternativeTo(Locale.ENGLISH),
            containsInAnyOrder(Locale.FRANCE, Locale.CANADA_FRENCH)
        )
    }
}
