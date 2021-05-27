import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kickstart.validation.MessageKeys
import kickstart.validation.getValue
import kickstart.validation.literal
import org.junit.jupiter.api.Test


val foo by literal

object Errors : MessageKeys() {
    val foo by literal
    val bar by literal
}


class MessageKeysTest {
    val bar by literal

    @Test
    fun `keys defined as variables`() {
        assertThat(foo.key, equalTo("foo"))
        assertThat(bar.key, equalTo("bar"))
    }

    @Test
    fun `keys in singleton objects`() {
        assertThat(Errors.foo.key, equalTo("errors.foo"))
        assertThat(Errors.bar.key, equalTo("errors.bar"))
    }


    val foo_bar by literal

    @Test
    fun `underscores are replaced with hyphens`() {
        assertThat(foo_bar.key, equalTo("foo-bar"))
    }

    object outer : MessageKeys() {
        object inner : MessageKeys() {
            val baz by literal
        }
    }

    @Test
    fun `definitions in deep nested keys`() {
        assertThat(outer.inner.baz.key, equalTo("outer.inner.baz"))
    }
}