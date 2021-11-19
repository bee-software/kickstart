package security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kotlin.test.Test

class HexTest {
    @Test
    fun `encoding to hex`() {
        assertThat("encoding",
            "Lorem ipsum dolor sit amet".toHex(upperCase = true),
            equalTo("4C6F72656D20697073756D20646F6C6F722073697420616D6574")
        )
    }

    @Test
    fun `round tripping`() {
        assertThat("round trip",
            "01ac3f4e418d6a5b19ed".fromHex().toHex(false),
            equalTo("01ac3f4e418d6a5b19ed")
        )
    }
}
