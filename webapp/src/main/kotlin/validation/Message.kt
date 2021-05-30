package kickstart.validation

import kickstart.i18n.Lookup


typealias Args = (String, Lookup) -> String

data class Message(val key: String, private val args: Args = noArgs) {
    fun getBy(lookup: Lookup) = args(key, lookup)

    companion object {
        fun normalized(key: String, args: Args) = Message(key.replace('_', '-'), args)
    }
}

val noArgs = object : Args {
    override fun invoke(key: String, lookup: Lookup) = lookup(key, arrayOf())

    override fun toString() = "none"
}