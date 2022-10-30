package kickstart

import com.vtence.molecule.testing.BodyContent
import it.skrape.core.htmlDocument
import it.skrape.selects.Doc
import kickstart.i18n.i18n
import java.nio.file.Path
import java.util.*


private class ViewRenderer<in T : Any>(from: Path, private val locale: Locale) {
    val pages = pages(from)
    val i18n = i18n(Locale.getDefault(), locale)

    fun render(template: String, context: T): Doc {
        val response = i18n.localize(pages, locale).named<T>(template).render(context)
        return htmlDocument(BodyContent.asText(response))
    }
}


typealias Expectations = Doc.() -> Unit


open class PageFixture<T : Any>(
    private val templateName: String,
    private val content: T,
    protected val locale: Locale,
) {
    private val renderer = ViewRenderer<T>(WebRoot.location, locale)

    fun render(relaxed: Boolean = false, expectations: Expectations): Doc {
        val doc = renderer.render(templateName, content)
        doc.relaxed = relaxed
        return doc.apply(expectations)
    }
}
