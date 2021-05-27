package kickstart.i18n

import kickstart.View
import kickstart.Views
import java.util.*

class I18n(private val supportedLocales: Set<Locale>, location: String) {
    init {
        if (supportedLocales.isEmpty()) throw IllegalArgumentException("No supported locales given")
    }

    private val messages = BundledMessages.rootedAt(location)

    val defaultLocale get() = supportedLocales.first()

    fun localize(views: Views, locale: Locale): Views {
        return object : Views {
            override fun <T : Any> named(name: String): View<T> {
                return View { content ->
                    val view = views.named<Any>(name)
                    if (content is I18ned) {
                        val translations = messages.loadBundle("views", name, locale)
                        view.render(content.localize(Translations(locale, alternativeLocales(locale), translations)))
                    } else {
                        view.render(content)
                    }
                }
            }
        }
    }

    private fun alternativeLocales(current: Locale) = supportedLocales.filterNot { it == current }.toSet()
}

fun i18n(defaultLocale: Locale, vararg alternativeLocales: Locale) =
    I18n(setOf(defaultLocale) + alternativeLocales.toSet(), "i18n")