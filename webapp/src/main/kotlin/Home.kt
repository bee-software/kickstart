package kickstart

import kickstart.i18n.I18ned
import kickstart.i18n.LocalizedMessages
import kickstart.i18n.noMessages
import kickstart.validation.interpolation


data class Home(private val messages: LocalizedMessages = noMessages): I18ned {
    val lang by messages::language
    val t by messages::interpolation

    override fun localize(messages: LocalizedMessages) = Home(messages)
}