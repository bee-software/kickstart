package kickstart.security

import kickstart.i18n.I18ned
import kickstart.i18n.LocalizedMessages
import kickstart.i18n.noMessages
import kickstart.validation.*

data class Login(
    val username: String? = null,
    private val errors: List<Message> = listOf(),
    private val messages: LocalizedMessages = noMessages
) : I18ned {
    val lang by messages::language
    val t by messages::interpolation

    override fun localize(messages: LocalizedMessages) = copy(messages = messages)

    fun errors() = ErrorMessages(prefix = "errors", messages = errors, lookup = messages.lookup)

    operator fun plus(error: Message) = copy(errors = errors + error)

    companion object {
        val empty = Login()

        fun invalid(username: String?) = Login(username) + Errors.Login.Credentials.invalid
    }
}


object Errors : MessageKeys() {
    object Login : MessageKeys() {
        object Credentials : MessageKeys() {
            val invalid by literal
        }
    }
}
