package kickstart.security

import java.math.BigInteger
import java.security.MessageDigest


@JvmInline
value class HashCode(private val bytes: ByteArray) {
    fun toHex() = BigInteger(1, bytes).toString(16).padStart(bytes.size * 2, '0')

    override fun toString() = toHex()
}


typealias HashFunction = (ByteArray) -> HashCode


object MD5: HashFunction {
    private val hasher: MessageDigest = MessageDigest.getInstance("MD5")

    override operator fun invoke(bytes: ByteArray): HashCode {
        return HashCode(hasher.digest(bytes))
    }
}


operator fun HashFunction.invoke(content: String): HashCode = this(content.encodeToByteArray())
