package kickstart.i18n

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.Test
import java.util.*

class MessageBundleTest {
    val messages = MessageBundle(object : ListResourceBundle() {
        override fun getContents(): Array<Array<Any>> {
            return arrayOf(
                arrayOf("required", "is required"),
                arrayOf("out-of-range", "must be between {0, number} and {1, number}"),
                arrayOf("expired", "must be after {0, date}")
            )
        }

        override fun getLocale(): Locale {
            return Locale.CANADA_FRENCH
        }
    })

    @Test
    fun `reads messages from resource bundle`() {
        assertThat("formatted message", messages.interpolate("required"), equalTo("is required"))
    }

    @Test
    fun `uses provided parameters as message arguments`() {
        assertThat(
            "formatted message",
            messages.interpolate("out-of-range", 1, 10),
            equalTo("must be between 1 and 10")
        )
    }

    @Test
    fun `uses bundle locale when formatting parameters`() {
        val moment = LocalDate.parse("2021-05-05").atStartOfDay().toInstant(ZoneOffset.ofHours(-4))
        assertThat(
            "formatted message",
            messages.interpolate("expired", Date.from(moment)),
            equalTo("must be after 5 mai 2021")
        )
    }
}
