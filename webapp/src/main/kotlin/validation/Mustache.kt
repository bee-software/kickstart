package kickstart.validation

import com.samskivert.mustache.Mustache
import kickstart.i18n.Lookup


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
