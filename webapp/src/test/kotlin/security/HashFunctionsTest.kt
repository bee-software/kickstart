package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.minutest.Tests
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import dev.minutest.test

class HashFunctionsTest : JUnit5Minutests {
    @Tests
    fun tests() = rootContext {
        context("MD5") {
            test("computes checksum of content") {
                assertThat(
                    "md5", MD5("lorem ipsum").toHex(), equalTo("80a751fde577028640c419000e33eba6")
                )
            }

            test("pads checksum with zeros") {
                assertThat(
                    "md5", MD5("363").toHex(), equalTo("00411460f7c92d2124a67ea0f4cb5f85")
                )
            }
        }
    }
}
