package kickstart.security

import java.math.BigInteger
import java.security.MessageDigest


@JvmInline
value class HashCode(private val bytes: ByteArray) {

    fun toHex() = BigInteger(1, bytes).toString(16).padStart(bytes.size * 2, '0')

    override fun toString() = toHex()
}


abstract class HashFunction {

    fun hash(content: String): HashCode = hash(content.encodeToByteArray())

    fun hash(bytes: ByteArray) = HashCode(hasher.digest(bytes))

    protected abstract val hasher: MessageDigest

    object md5: HashFunction() {
        override val hasher: MessageDigest = MessageDigest.getInstance("MD5")
    }
}


fun String.md5() = HashFunction.md5.hash(this).toHex()
