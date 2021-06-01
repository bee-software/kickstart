package kickstart

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import kickstart.validation.ValidationResult
import kickstart.validation.Violation


fun <T> containsAll(elements: Iterable<T>) = Matcher(Collection<T>::containsAll, elements.toList())

fun <T> containsAll(vararg elements: T) = containsAll(elements.toList())


fun <T> isFailure(matching: Matcher<List<Violation>>) =
    cast(has(ValidationResult.Failure<T>::violations, matching))

fun <T> isFailure(violations: List<Violation>) = isFailure<T>(equalTo(violations))

fun <T> isFailure(vararg violations: Violation) = isFailure<T>(violations.toList())
