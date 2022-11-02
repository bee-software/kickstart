package kickstart.i18n

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class MessageBundleTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext< MessageBundle> {
        given {
            MessageBundle(object : ListResourceBundle() {
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
        }

        test("reads messages from resource bundle") {
            assertThat("formatted message", interpolate("required"), equalTo("is required"))
        }

        test("uses provided parameters as message arguments") {
            assertThat(
                "formatted message",
                interpolate("out-of-range", 1, 10),
                equalTo("must be between 1 and 10")
            )
        }

        test("uses bundle locale when formatting parameters") {
            val moment = LocalDate.parse("2021-05-05").atStartOfDay().toInstant(ZoneOffset.ofHours(-4))
            assertThat(
                "formatted message",
                interpolate("expired", Date.from(moment)),
                equalTo("must be after 5 mai 2021")
            )
        }
    }
}
