package kickstart.i18n

import java.util.*


interface I18ned {
    fun localize(messages: LocalizedMessages)
}


interface LocalizedMessages: Messages {
    val locale: Locale
    val alternatives: Set<Locale>

    val language: String get() = locale.language
    val alternativeLanguages: Iterable<String> get() = alternatives.map { it.language }
}


data class Translations(
    override val locale : Locale,
    override val alternatives: Set<Locale>,
    private val messages: Messages,
) : LocalizedMessages  {

    override fun searchPath(key: String) = messages.searchPath(key)

    override fun interpolate(key: String, vararg args: Any) = messages.interpolate(key, *args)
}


class MissingTranslationException(key: String, messages: Messages): RuntimeException() {
    override val message = "$key not found; searched:\n${messages.searchPath(key).description}"
}

private val List<MessageLocation>.description: String
    get() = joinToString(separator = "\n", postfix = "\n") { " - ${it.description} ${it.uri?.let { "($it)" } ?: ""}"  }


fun noMessagesIn(locale: Locale): LocalizedMessages = object : LocalizedMessages {
    override val locale : Locale = locale
    override val alternatives: Set<Locale> = setOf()

    override fun searchPath(key: String) = listOf<MessageLocation>()

    override fun interpolate(key: String, vararg args: Any): Nothing? = null

    override fun toString() = "no messages in ${locale.displayName}"
}

val noMessages = noMessagesIn(Locale.getDefault())
