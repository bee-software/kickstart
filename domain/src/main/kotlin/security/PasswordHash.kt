package security

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.experimental.xor

class PasswordHash private constructor(
    private val iterations: Int,
    private val salt: ByteArray,
    private val hash: ByteArray,
) {
    val value: String
        get() = "$iterations:${salt.toHex()}:${hash.toHex()}"

    fun checkPassword(candidate: String): Boolean {
        // Compute the hash of the provided password, using the same salt, iteration count, and hash length
        val candidateHash = candidate.encrypt(salt, iterations, hash.size)
        // Passwords match if hashes match
        return hash.slowEquals(candidateHash)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordHash

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    companion object {
        private const val SALT_BYTE_SIZE = 24
        private const val HASH_BYTE_SIZE = 24
        private const val ITERATIONS = 1000

        fun from(value: String): PasswordHash {
            val params = value.split(":").toTypedArray()
            require(params.size == 3) { value }
            val (iterations, salt, hash) = params
            return PasswordHash(iterations.toInt(), salt.fromHex(), hash.fromHex())
        }

        fun create(password: String): PasswordHash {
            val salt = generateSalt(SALT_BYTE_SIZE)
            val hash = password.encrypt(salt, ITERATIONS, HASH_BYTE_SIZE)
            return PasswordHash(ITERATIONS, salt, hash)
        }
    }
}


private fun generateSalt(size: Int) = SecureRandom().run {
    ByteArray(size).also { nextBytes(it) }
}

private fun String.encrypt(salt: ByteArray, iterations: Int, hashSize: Int): ByteArray {
    return toCharArray().encrypt(salt, iterations, hashSize)
}

private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1"

private fun CharArray.encrypt(salt: ByteArray, iterations: Int, hashSize: Int): ByteArray {
    val spec = PBEKeySpec(this, salt, iterations, hashSize * 8)
    val algo = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
    return algo.generateSecret(spec).encoded
}

private fun ByteArray.slowEquals(other: ByteArray): Boolean {
    // Compare arrays in constant time
    var diff = size xor other.size
    var i = 0
    while (i < size && i < other.size) {
        diff = diff or ((this[i] xor other[i]).toInt())
        i++
    }
    return diff == 0
}