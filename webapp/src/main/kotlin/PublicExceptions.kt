package kickstart

import com.vtence.molecule.Application
import com.vtence.molecule.Middleware
import com.vtence.molecule.Response
import com.vtence.molecule.lib.FileBody
import kickstart.i18n.locale
import java.io.File
import java.nio.file.Path
import java.util.*

class PublicExceptions(private val assets: Path) : Middleware {
    override fun then(next: Application): Application {
        return Application { request ->
            next.handle(request).whenSuccessful(renderErrorPageIn(request.locale))
        }
    }

    private fun renderErrorPageIn(locale: Locale?): (Response) -> Unit = { response ->
        val page = locale?.let { localizedErrorPage(response.statusCode(), it) }
            ?: defaultErrorPageFor(response.statusCode())

        page?.let { renderHtmlFile(response, it) }
    }

    private fun localizedErrorPage(statusCode: Int, locale: Locale): File? {
        return lookupPage("${statusCode}_${locale.language}.html")
    }

    private fun defaultErrorPageFor(statusCode: Int): File? = lookupPage("${statusCode}.html")

    private fun lookupPage(name: String) = assets.resolve(name).toFile().takeIf { it.exists() }

    private fun renderHtmlFile(response: Response, errorPage: File) {
        response.contentLength(errorPage.length())
        response.contentType("text/html; charset=utf-8")
        response.body(FileBody(errorPage))
    }
}
