package kickstart

import com.vtence.molecule.Response
import com.vtence.molecule.templating.JMustacheRenderer
import com.vtence.molecule.templating.Templates
import java.nio.file.Path


fun interface View<in T : Any> {
    fun render(model: T): Response
}

fun <T : Any> View<T>.done(result: T): Response = render(result).done()


interface Views {
    fun <T : Any> named(name: String): View<T>
}

class Pages(folder: Path): Views  {

    private val templates: Templates = Templates(JMustacheRenderer.fromDir(folder.toFile()).nullValue(""))

    override fun <T : Any> named(name: String): View<T> {
        return View {
            val template = templates.named<T>("$name.html")

            Response.ok()
                .contentType("text/html; charset=utf-8")
                .body(template.render(it))
        }
    }

    companion object {
        fun inDir(folder: Path) = Pages(folder)
    }
}

fun pages(root: Path) = Pages(root.resolve("app").resolve("views"))