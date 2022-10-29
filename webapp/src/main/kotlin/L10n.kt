package kickstart

import com.samskivert.mustache.Mustache
import kickstart.i18n.*

data class L10n(
    private var messages: LocalizedMessages = noMessages
) : L10ned, I18ned {

    override val lang get() = messages.language
    override val t get() = messages.interpolation

    fun lookup(key: String, vararg args: Any) = messages.lookup(key, args)

    override fun localize(messages: LocalizedMessages) {
        this.messages = messages
    }
}


interface L10ned {
    val lang : String
    val t : Mustache.Lambda
}