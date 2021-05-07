package kickstart

import com.vtence.molecule.Response
import com.vtence.molecule.templating.JMustacheRenderer
import com.vtence.molecule.templating.Templates
import java.io.File
import java.nio.file.Path


fun interface View<in T : Any> {
    fun render(model: T): Response
}

class Pages(folder: File)  {

    private val templates: Templates = Templates(JMustacheRenderer.fromDir(folder).nullValue(""))

    fun <T : Any> named(name: String): View<T> {
        return View {
            val template = templates.named<T>("$name.html")

            Response.ok()
                .contentType("text/html; charset=utf-8")
                .done(template.render(it))
        }
    }

    companion object {
        fun inDir(folder: Path) = Pages(folder.toFile())
    }
}