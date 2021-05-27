package kickstart.validation

import kotlin.reflect.KProperty


open class MessageKeys {
    private val outer = javaClass.enclosingClass?.kotlin?.objectInstance as? MessageKeys
    private val name : String get() = prefix + groupName
    private val prefix = outer?.name?.let { "$it." } ?: ""
    private val groupName get() =
        this::class.simpleName?.substringBefore("$")?.replaceFirstChar { it.lowercase() }
            ?: throw IllegalArgumentException("cannot determine name of keys")

    fun message(key: String, args: Args): Message {
        return Message.normalized("$name.$key", args)
    }
}


operator fun <K : MessageKeys> Args.getValue(keys: K, property: KProperty<*>) =
    keys.message(property.name, this)

operator fun <SCOPE> Args.getValue(scope: SCOPE, property: KProperty<*>) =
    Message.normalized(property.name, this)


val literal = noArgs
