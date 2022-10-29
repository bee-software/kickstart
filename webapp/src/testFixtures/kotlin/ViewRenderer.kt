package kickstart

import com.vtence.molecule.testing.BodyContent
import it.skrape.core.htmlDocument
import it.skrape.selects.Doc
import kickstart.i18n.i18n
import java.nio.file.Path
import java.util.*

class ViewRenderer<in T : Any>(from: Path, private val locale: Locale) {
    val pages = pages(from)
    val i18n = i18n(Locale.getDefault(), locale)

    fun render(template: String, context: T): Doc {
        val response = i18n.localize(pages, locale).named<T>(template).render(context)
        return htmlDocument(BodyContent.asText(response))
    }
}


typealias Expectations = Doc.() -> Unit

inline fun <reified T : Any> renderView(
    template: String,
    context: T,
    locale: Locale = Locale.getDefault(),
    expectations: Expectations = {}
): Doc {
    val renderer = ViewRenderer<T>(WebRoot.location, locale)
    val doc = renderer.render(template, context)
    return doc.apply(expectations)
}