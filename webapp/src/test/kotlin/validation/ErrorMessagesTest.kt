package validation

import com.natpryce.hamkrest.assertion.assertThat
import kickstart.validation.Message
import kickstart.validation.ErrorMessages
import org.junit.jupiter.api.Test

class ErrorMessagesTest {
    @Test
    fun `looks up keys by prefix and name`() {
        val errors = ErrorMessages(
            "fruit", listOf(

                Message("fruit.citrus.lime"),
                Message("fruit.citrus.orange.navel"),
                Message("fruit.citrus.orange.blood"),
                Message("fruit.berry.raspberry"),
                Message("fruit.berry.blueberry"),

                Message("other.berry.blueberry"),
            )
        ) { key, _ -> key }

        val citrus = errors["citrus"]
        assertThat("fruit.citrus.*", citrus.toList(), List<String>::containsAll, listOf(
            "fruit.citrus.lime",
            "fruit.citrus.orange.navel",
            "fruit.citrus.orange.blood",
        ))

        val berries = errors["berry"]
        assertThat("fruit.berry.*", berries.toList(), List<String>::containsAll, listOf(
            "fruit.berry.raspberry",
            "fruit.berry.blueberry",
        ))

        val oranges = citrus["orange"]
        assertThat("fruit.citrus.orange.*", oranges.toList(), List<String>::containsAll, listOf(
            "fruit.citrus.orange.blood",
            "fruit.citrus.orange.navel",
        ))
    }
}