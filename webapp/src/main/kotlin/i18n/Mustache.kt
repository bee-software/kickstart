package kickstart.i18n

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import java.io.Writer


typealias Lookup = (String, Array<out Any>) -> String


val Messages.lookup: Lookup get() = { key, args ->
    interpolate(key, args) ?: throw MissingTranslationException(key, this)
}


class Interpolation(private val lookup: Lookup) : Mustache.Lambda {

    override fun execute(frag: Template.Fragment, out: Writer) {
        out.write(lookup(frag.execute().trim { it <= ' ' }, arrayOf()))
    }
}

val Messages.interpolation get() = Interpolation(lookup)
