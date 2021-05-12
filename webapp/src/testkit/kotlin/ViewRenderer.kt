import com.vtence.molecule.testing.BodyContent
import it.skrape.core.htmlDocument
import it.skrape.selects.Doc
import kickstart.i18n.LocalizedContent
import kickstart.i18n.i18n
import kickstart.pages
import java.nio.file.Path
import java.util.*

class ViewRenderer<in T : Any>(from: Path) {

    val pages = pages(from)
    val i18n = i18n(Locale.getDefault())

    fun render(template: String, context: T): Doc {
        val locale = when(context) {
            is LocalizedContent -> context.locale
            else -> Locale.getDefault()
        }

        val response = i18n.localize(pages, locale).named<T>(template).render(context)
        return htmlDocument(BodyContent.asText(response))
    }

    companion object {
        fun <T : Any> from(location: Path): ViewRenderer<T> {
            return ViewRenderer(location)
        }
    }
}

typealias Expectations = Doc.() -> Unit

inline fun <reified T : Any> renderView(template: String, context: T, expectations: Expectations = {}): Doc {
    val renderer = ViewRenderer.from<T>(WebRoot.location)
    val doc = renderer.render(template, context)
    return doc.apply(expectations)
}