package kickstart.validation

import kickstart.i18n.Lookup
import kickstart.validation.ValidationResult.*


@JvmInline
value class Key(val name: String) {
    companion object {
        fun named(key: String) = Key(key.replace('_', '-'))
    }
}

data class Violation(val key: Key, private val args: List<Any> = listOf()) {
    constructor(key: Key, vararg args: Any): this(key, args.toList())

    fun messageBy(lookup: Lookup) = lookup(key.name, args.toTypedArray())
}

fun violation(key: String, vararg args: Any) = Violation(Key.named(key), args)


sealed class ValidationResult<T> {
    class Success<T>(val value: T) : ValidationResult<T>()
    class Failure<T>(val violations: List<Violation>) : ValidationResult<T>() {
        constructor(vararg violations: Violation): this(violations.toList())
    }
}

fun <T, V, R> ValidationResult<T>.and(other: ValidationResult<V>, combine: (T, V) -> R): ValidationResult<R> {
    return when (this) {
        is Success ->
            when (other) {
                is Success -> Success(combine(value, other.value))
                is Failure -> Failure(other.violations)
            }
        is Failure ->
            when (other) {
                is Success -> Failure(violations)
                is Failure -> Failure(violations + other.violations)
            }
    }
}

operator fun <T> ValidationResult<T>.plus(other: ValidationResult<T>): ValidationResult<List<T>> {
    return this.and(other) { left, right -> listOf(left, right) }
}


interface Validator<T, R>: (T) -> ValidationResult<R> {
    val key: Key
}

typealias ValidationRule<T, R> = (Key) -> Validator<T, R>