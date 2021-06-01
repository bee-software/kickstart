package kickstart.validation

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kotlin.test.Test

val foo by notBlank

object Errors : ValidationKeys() {
    val foo by notBlank
    val bar by failure
}


class ValidationKeysTest {
    val bar by failure

    @Test
    fun `keys defined as variables`() {
        assertThat(foo.key, equalTo(Key("foo")))
        assertThat(bar.key, equalTo(Key("bar")))
    }

    @Test
    fun `keys in singleton objects`() {
        assertThat(Errors.foo.key, equalTo(Key("errors.foo")))
        assertThat(Errors.bar.key, equalTo(Key("errors.bar")))
    }


    val foo_bar by failure

    @Test
    fun `underscores are replaced with hyphens`() {
        assertThat(foo_bar.key, equalTo(Key("foo-bar")))
    }

    object outer : ValidationKeys() {
        object inner : ValidationKeys() {
            val baz by failure
        }
    }

    @Test
    fun `definitions in deep nested keys`() {
        assertThat(outer.inner.baz.key, equalTo(Key("outer.inner.baz")))
    }
}