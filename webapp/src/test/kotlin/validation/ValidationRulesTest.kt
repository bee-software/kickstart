package kickstart.validation

import com.natpryce.hamkrest.assertion.assertThat
import dev.minutest.Tests
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import kickstart.isFailure


class ValidationRulesTest : JUnit5Minutests {
    @Tests
    fun `failure tests`() = rootContext {
        test("always fails, reporting offending value") {
            val validate = failure(Key("invalid"))
            val result = validate("value")
            assertThat(result, isFailure<String>(Violation(Key("invalid"), listOf("value"))))
        }
    }

    @Tests
    fun `not blank tests`() = rootContext {
        context("value is null") {
            test("fails and reports no value") {
                val validate = notBlank(Key("null"))
                val result = validate(null)
                assertThat(result, isFailure<String>(Violation(Key("null"))))
            }
        }

        context("value is blank") {
            test("fails if value is blank, still reporting no value") {
                val validate = notBlank(Key("blank"))
                val result = validate("   ")
                assertThat(result, isFailure<String>(Violation(Key("blank"))))
            }
        }
    }
}