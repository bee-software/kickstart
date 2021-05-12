package kickstart.i18n

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import java.io.Writer
import java.util.*

class LocalizedContent: Localized {

    private val alternativeLocales: MutableSet<Locale> = HashSet()

    private var translations: Messages = compose()

    var locale: Locale = Locale.getDefault()
    val lang: String get() = locale.language
    val languages: Iterable<String> = alternativeLocales.map { it.language }

    fun t(): Mustache.Lambda {
        return Mustache.Lambda { frag: Template.Fragment, out: Writer ->
            out.write(
                translations.interpolate(frag.execute().trim { it <= ' ' })
            )
        }
    }

    override fun switchLocale(locale: Locale, alternatives: Set<Locale>, translations: Messages) {
        this.locale = locale
        this.alternativeLocales += alternatives
        this.translations = translations
    }
}