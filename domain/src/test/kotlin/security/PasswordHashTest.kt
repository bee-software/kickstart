package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import kotlin.test.Test

class PasswordHashTest {
    @Test
    fun `creates hash from clear text password`() {
        val hash = PasswordHash.create("clear password")
        assertThat("hash value", hash.value, !equalTo("clear password"))
        assertThat("valid password", hash.validate("clear password"), equalTo(true))
        assertThat("invalid password", hash.validate("wrong password"), equalTo(false))

        val same = PasswordHash.from(hash.value)
        assertThat("same hash", same.validate("clear password"), equalTo(true))
    }

    @Test
    fun `generates a 24 bytes salt`() {
        val hash = PasswordHash.create("password")
        val salt = salt(hash)
        assertThat("salt size", salt.length, equalTo(24 * 2))
    }

    @Test
    fun `generates unique random salts`() {
        val salts = arrayListOf<String>()
        repeat(100) {
            val hash = PasswordHash.create("password")
            val salt = salt(hash)
            assertThat("salt collision", salts, !hasElement(salt))
            salts.add(salt)
        }
    }

    private fun salt(hash: PasswordHash): String {
        val params = hash.value.split(":")
        assertThat("hash components", params, hasSize(equalTo(3)))
        val (_, salt, _) = params
        return salt
    }
}