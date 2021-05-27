package kickstart.validation

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import kickstart.i18n.Messages
import java.io.Writer


class Interpolation(private val lookup: Lookup) : Mustache.Lambda {

    override fun execute(frag: Template.Fragment, out: Writer) {
        out.write(lookup(frag.execute().trim { it <= ' ' }, arrayOf()))
    }
}

val Messages.interpolation get() = Interpolation(lookup)


class ErrorMessages(
    private val prefix: String = "",
    private val messages: List<Message>,
    private val lookup: Lookup
) : Mustache.CustomContext, Iterable<String> {

    override fun iterator(): Iterator<String> {
        return messages.map { it.getBy(lookup) }.iterator()
    }

    override fun get(name: String): ErrorMessages {
        return ErrorMessages("$prefix.$name", messages.filter { it.key.startsWith("$prefix.$name.") }, lookup)
    }
}
