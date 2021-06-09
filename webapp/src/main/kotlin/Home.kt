package kickstart

import kickstart.i18n.I18ned
import kickstart.i18n.LocalizedMessages
import kickstart.i18n.interpolation
import kickstart.i18n.noMessages
import kickstart.security.Username
import kickstart.security.md5


data class Home(
    val username: Username? = null,
    private val messages: LocalizedMessages = noMessages
): I18ned {
    val lang by messages::language
    val t by messages::interpolation

    val gravatar get() = username.toString().md5()

    override fun localize(messages: LocalizedMessages) = copy(messages = messages)
}
