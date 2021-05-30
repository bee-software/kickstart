package kickstart.i18n

import java.net.URI


data class Location(val description: String, val uri: URI? = null) {
    companion object {
        fun intrinsic(description: String = "intrinsic") = Location(description)
    }
}


data class MessageLocation(val key: String, val location: Location) {
    val description by location::description
    val uri by location::uri
}


interface Messages {
    fun interpolate(key: String, vararg args: Any): String?

    fun searchPath(key: String): List<MessageLocation>

    companion object {
        operator fun invoke(interpolate: (String, Array<out Any>) -> String?) = object : Messages {
            override fun interpolate(key: String, vararg args: Any) = interpolate(key, args)
            override fun searchPath(key: String) = listOf(MessageLocation(key, Location.intrinsic()))
        }
    }
}


class CompositeMessages(private val messages: List<Messages>) : Messages {

    override fun interpolate(key: String, vararg args: Any): String? {
        return messages.firstNotNullOfOrNull {
            kotlin.runCatching { it.interpolate(key) }.getOrNull()
        }
    }

    override fun searchPath(key: String): List<MessageLocation> {
        return messages.flatMap { it.searchPath(key) }
    }

    fun add(other: Messages): Messages  = CompositeMessages(messages + other)
}

fun compose(vararg messages: Messages) = CompositeMessages(messages.toList())

operator fun Messages.plus(messages: Messages) = compose(this, messages)
