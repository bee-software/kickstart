package kickstart.validation

import com.natpryce.hamkrest.assertion.assertThat
import kickstart.containsAll
import org.junit.jupiter.api.Test

class ErrorMessagesTest {
    @Test
    fun `looks up keys by prefix and name`() {
        val errors = ErrorMessages(
            "fruit", listOf(
                violation("fruit.citrus.lime"),
                violation("fruit.citrus.orange.navel"),
                violation("fruit.citrus.orange.blood"),
                violation("fruit.berry.raspberry"),
                violation("fruit.berry.blueberry"),

                violation("other.berry.blueberry"),
            )
        ) { key, _ -> key }

        val citrus = errors["citrus"]
        assertThat("fruit.citrus.*", citrus.toList(), containsAll(
            "fruit.citrus.lime",
            "fruit.citrus.orange.navel",
            "fruit.citrus.orange.blood",
        ))

        val berries = errors["berry"]
        assertThat("fruit.berry.*", berries.toList(), containsAll(
            "fruit.berry.raspberry",
            "fruit.berry.blueberry",
        ))

        val oranges = citrus["orange"]
        assertThat("fruit.citrus.orange.*", oranges.toList(), containsAll(
            "fruit.citrus.orange.blood",
            "fruit.citrus.orange.navel",
        ))
    }
}