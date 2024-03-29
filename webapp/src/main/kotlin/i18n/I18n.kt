package kickstart.i18n

import kickstart.View
import kickstart.Views
import java.util.*

class I18n(val supportedLocales: Set<Locale>, location: String) {
    init {
        require(supportedLocales.isNotEmpty()) { "No supported locales specified" }
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
                        content.localize(Translations(locale, alternativeTo(locale), translations))
                        view.render(content)
                    } else {
                        view.render(content)
                    }
                }
            }
        }
    }

    fun alternativeTo(locale: Locale) = supportedLocales.filterNot { it == locale }.toSet()
}

fun i18n(defaultLocale: Locale, vararg alternativeLocales: Locale) =
    I18n(setOf(defaultLocale) + alternativeLocales.toSet(), "i18n")
