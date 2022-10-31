package kickstart.validation

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import kickstart.validation.ValidationFixture.*

val foo by notBlank


class ValidationFixture {
    val bar by failure
    val foo_bar by failure

    object Errors : ValidationKeys() {
        val foo by notBlank
        val bar by failure
    }


    object outer : ValidationKeys() {
        object inner : ValidationKeys() {
            val baz by failure
        }
    }
}


class ValidationKeysTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<ValidationFixture> {
        given { ValidationFixture() }

        test("keys defined as variables") {
            assertThat(foo.key, equalTo(Key("foo")))
            assertThat(bar.key, equalTo(Key("bar")))
        }

        test("keys in singleton objects") {
            assertThat(Errors.foo.key, equalTo(Key("errors.foo")))
            assertThat(Errors.bar.key, equalTo(Key("errors.bar")))
        }

        test("underscores are replaced with hyphens") {
            assertThat(foo_bar.key, equalTo(Key("foo-bar")))
        }

        test("definitions in deep nested keys") {
            assertThat(outer.inner.baz.key, equalTo(Key("outer.inner.baz")))
        }
    }
}