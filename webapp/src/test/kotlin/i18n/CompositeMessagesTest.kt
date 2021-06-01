package kickstart.i18n

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kotlin.test.Test

class CompositeMessagesTest {

    @Test
    fun `starts as empty`() {
        val composite = compose()

        assertThat("any message", composite.interpolate("message"), absent())
    }

    @Test
    fun `looks up translation in all composed messages`() {
        val composite = noMessages + Messages { _, _ -> "translation" }

        assertThat("translated message", composite.interpolate("message"), equalTo("translation"))
    }

    @Test
    fun `looks ups translations in order of addition`() {
        val composite = Messages { _, _ -> "original" } + Messages { _, _ -> "override" }

        assertThat("original translation", composite.interpolate("message"), equalTo("original"))
    }
}