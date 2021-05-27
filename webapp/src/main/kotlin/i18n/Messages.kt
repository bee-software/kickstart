package kickstart.i18n

import java.util.*

fun interface Messages {
    fun interpolate(key: String, vararg args: Any): String
}


class CompositeMessages(private val composition: List<Messages>) : Messages {

    override fun interpolate(key: String, vararg args: Any): String {
        composition.forEach {
            try {
                return it.interpolate(key)
            } catch (ignored: MissingResourceException) {
            }
        }

        throw MissingResourceException("Missing translation for key '$key'", javaClass.name, key)
    }

    fun add(messages: Messages): Messages  = CompositeMessages(composition + messages)
}

fun compose(vararg messages: Messages) = CompositeMessages(messages.toList())

operator fun Messages.plus(messages: Messages) = compose(this, messages)