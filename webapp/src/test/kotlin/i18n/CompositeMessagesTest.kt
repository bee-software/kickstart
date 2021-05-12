package kickstart.i18n

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CompositeMessagesTest {

    @Test
    fun `starts as empty`() {
        assertThrows<MissingResourceException> { compose().interpolate("key") }
    }

    @Test
    fun `looks up translation in all composed messages`() {
        val composite =
            Messages { message, _ ->
                throw MissingResourceException(
                    message,
                    null,
                    message
                )
            } + Messages { _, _ -> "translation" }

        assertThat("translated message", composite.interpolate("message"), equalTo("translation"))
    }

    @Test
    fun `looks ups translations in order of addition`() {
        val composite = Messages { _, _ -> "original" } + Messages { _, _ -> "override" }

        assertThat("original translation", composite.interpolate("message"), equalTo("original"))
    }
}