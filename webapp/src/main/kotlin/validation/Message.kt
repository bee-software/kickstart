package kickstart.validation

import kickstart.i18n.Messages


typealias Lookup = (String, Array<out Any>) -> String

val Messages.lookup get() = this::interpolate


typealias Args = (String, Lookup) -> String

data class Message(val key: String, private val args: Args = noArgs) {
    fun getBy(lookup: Lookup) = args(key, lookup)

    companion object {
        fun normalized(key: String, args: Args) = Message(key.replace('_', '-'), args)
    }
}

val noArgs: Args = { key, lookup -> lookup(key, arrayOf()) }