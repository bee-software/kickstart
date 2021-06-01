package kickstart.validation

import kotlin.reflect.KProperty


open class ValidationKeys {
    private val outer = javaClass.enclosingClass?.kotlin?.objectInstance as? ValidationKeys
    private val name: String get() = prefix + groupName
    private val prefix = outer?.name?.let { "$it." } ?: ""
    private val groupName
        get() = this::class.simpleName
            ?.substringBefore("$")
            ?.replaceFirstChar { it.lowercase() }
            ?: throw IllegalArgumentException("cannot determine name of keys")

    fun key(name: String): Key {
        return Key.named("${this.name}.$name")
    }
}


operator fun <K : ValidationKeys, T, R : Any> ValidationRule<T, R>.getValue(keys: K, property: KProperty<*>): Validator<T, R> =
    this(keys.key(property.name))


operator fun <SCOPE, T, R : Any> ValidationRule<T, R>.getValue(scope: SCOPE, property: KProperty<*>): Validator<T, R> =
    this(Key.named(property.name))