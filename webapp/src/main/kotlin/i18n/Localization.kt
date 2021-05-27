package kickstart.i18n

import java.util.*

interface I18ned {
    fun localize(messages: LocalizedMessages): Any
}


interface LocalizedMessages: Messages {
    val locale: Locale
    val alternatives: Set<Locale>

    val language: String get() = locale.language
    val alternativeLanguages: Iterable<String> get() = alternatives.map { it.language }
}

class Translations(
    override val locale : Locale,
    override val alternatives: Set<Locale> = setOf(),
    private val messages: Messages
) : LocalizedMessages  {

    override fun interpolate(key: String, vararg args: Any) = messages.interpolate(key, *args)
}

fun noMessagesIn(locale: Locale): LocalizedMessages = object : LocalizedMessages {
    override val locale : Locale = locale

    override val alternatives: Set<Locale> = setOf()

    override fun interpolate(key: String, vararg args: Any) =
        throw MissingResourceException("No translations defined", javaClass.name, key)
}

val noMessages = noMessagesIn(Locale.getDefault())
