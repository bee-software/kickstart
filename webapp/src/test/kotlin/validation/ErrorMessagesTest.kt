package kickstart.validation

import com.natpryce.hamkrest.assertion.assertThat
import dev.minutest.Tests
import dev.minutest.given
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test
import kickstart.containsAll
import kickstart.security.errors
import kotlin.test.Test

class ErrorMessagesTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext<ErrorMessages> {
        given { ErrorMessages(
            "fruit", listOf(
                violation("fruit.citrus.lime"),
                violation("fruit.citrus.orange.navel"),
                violation("fruit.citrus.orange.blood"),
                violation("fruit.berry.raspberry"),
                violation("fruit.berry.blueberry"),

                violation("other.berry.blueberry"),
            )
        ) { key, _ -> key } }

        test("looks up keys by prefix and name") {
            val citrus = it["citrus"]
            assertThat("fruit.citrus.*", citrus.toList(), containsAll(
                "fruit.citrus.lime",
                "fruit.citrus.orange.navel",
                "fruit.citrus.orange.blood",
            ))

            val berries = it["berry"]
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
}