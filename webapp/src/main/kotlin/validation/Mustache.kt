package kickstart.validation

import com.samskivert.mustache.Mustache
import kickstart.i18n.Lookup


class ErrorMessages(
    private val prefix: String = "",
    private val violations: List<Violation>,
    private val lookup: Lookup
) : Mustache.CustomContext, Iterable<String> {

    override fun iterator(): Iterator<String> {
        return violations.map { it.messageBy(lookup) }.iterator()
    }

    override fun get(name: String): ErrorMessages {
        return ErrorMessages("$prefix.$name", violations.filter { it.key.name.startsWith("$prefix.$name.") }, lookup)
    }
}
