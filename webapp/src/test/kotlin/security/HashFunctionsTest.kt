package kickstart.security

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class HashFunctionsTest {
    @Test
    fun `computes md5 checksum of content`() {
        assertThat(
            "md5", "lorem ipsum".md5(), equalTo("80a751fde577028640c419000e33eba6")
        )
    }

    @Test
    fun `pads checksum with zeros`() {
        assertThat(
            "md5", "363".md5(), equalTo("00411460f7c92d2124a67ea0f4cb5f85")
        )
    }
}
