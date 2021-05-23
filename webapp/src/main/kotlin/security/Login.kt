package kickstart.security

import kickstart.i18n.Localized
import kickstart.i18n.Translations

data class Login(val username: String? = null, override var translations: Translations = Translations()) : Localized {
    val lang get() = translations.lang
    val t get() = translations.interpolation
}
