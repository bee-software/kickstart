package kickstart.i18n

import java.util.*

fun interface Localized {
    fun switchLocale(locale: Locale, alternatives: Set<Locale>, translations: Messages)
}