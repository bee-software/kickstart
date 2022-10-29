package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import javax.swing.text.html.parser.DTDConstants.MD

class HashFunctionsTest {
    @Test
    fun `computes md5 checksum of content`() {
        assertThat(
            "md5", MD5("lorem ipsum").toHex(), equalTo("80a751fde577028640c419000e33eba6")
        )
    }

    @Test
    fun `pads checksum with zeros`() {
        assertThat(
            "md5", MD5("363").toHex(), equalTo("00411460f7c92d2124a67ea0f4cb5f85")
        )
    }
}
