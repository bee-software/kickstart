package kickstart

import kickstart.i18n.Localized
import kickstart.i18n.Translations

data class Home(override var translations: Translations = Translations()): Localized {
    val lang get() = translations.lang
    val t get() = translations.interpolation
}