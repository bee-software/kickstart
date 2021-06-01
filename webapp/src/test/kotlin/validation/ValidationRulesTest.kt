package kickstart.validation

import com.natpryce.hamkrest.assertion.assertThat
import kickstart.isFailure
import org.junit.jupiter.api.Test

class FailureTest {
    @Test
    fun `always fails, reporting offending value`() {
        val validate = failure(Key("invalid"))
        val result = validate("value")
        assertThat(result, isFailure<String>(Violation(Key("invalid"), listOf("value"))))
    }
}

class NotBlankTest {
    @Test
    fun `fails if value is null, reporting no value`() {
        val validate = notBlank(Key("null"))
        val result = validate(null)
        assertThat(result, isFailure<String>(Violation(Key("null"))))
    }

    @Test
    fun `fails if value is blank, still reporting no value`() {
        val validate = notBlank(Key("blank"))
        val result = validate("   ")
        assertThat(result, isFailure<String>(Violation(Key("blank"))))
    }
}