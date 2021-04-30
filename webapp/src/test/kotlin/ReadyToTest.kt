package kickstart

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.serpro69.kfaker.Faker
import org.junit.jupiter.api.Test

class ReadyToTest {

    val faker = Faker()

    @Test
    fun `ready to test`() {
        assertThat(1 + 1, equalTo(2))
    }

    @Test
    fun `ready to fake`() {
        assertThat(faker.animal.name(), !Matcher(String::isBlank))
    }
}