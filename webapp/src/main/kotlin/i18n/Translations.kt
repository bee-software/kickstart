package kickstart.i18n

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import java.io.Writer
import java.util.*

class Translations(
    val locale: Locale = Locale.getDefault(),
    val alternatives: Set<Locale> = setOf(),
    val messages: Messages = compose()
) {
    val lang: String get() = locale.language
    val languages: Iterable<String> = alternatives.map { it.language }

    val interpolation: Mustache.Lambda
        get() {
            return Mustache.Lambda { frag: Template.Fragment, out: Writer ->
                out.write(
                    messages.interpolate(frag.execute().trim { it <= ' ' })
                )
            }
        }
}