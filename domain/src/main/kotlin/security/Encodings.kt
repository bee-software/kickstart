package kickstart.security

import java.util.*

class HexEncoder(upperCase: Boolean = true) {
    private val alphabet = "0123456789abcdef"
        .let { if (upperCase) it.uppercase(Locale.getDefault()) else it }
        .toCharArray()

    fun encodeToHex(bytes: ByteArray): String {
        val hex = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hex[i * 2] = alphabet[v ushr 4]
            hex[i * 2 + 1] = alphabet[v and 0x0F]
        }
        return String(hex)
    }
}

fun String.toHex(upperCase: Boolean = true) = encodeToByteArray().toHex(upperCase)

fun ByteArray.toHex(upperCase: Boolean = true) = HexEncoder(upperCase).encodeToHex(this)


object HexDecoder {
    fun decodeFromHex(hex: String): ByteArray = decode(hex.toCharArray())

    private fun decode(hex: CharArray): ByteArray {
        val len = hex.size
        require(len and 0x01 == 0) { "odd number of characters" }
        val out = ByteArray(len shr 1)

        // 2 characters form the hex value
        out.indices.forEach {
            out[it] = (toDigit(hex[2 * it]) shl 4 or toDigit(hex[2 * it + 1]) and 0xFF).toByte()
        }

        return out
    }

    private fun toDigit(c: Char): Int = Character.digit(c, 16)
}

fun String.fromHex() = HexDecoder.decodeFromHex(this)
