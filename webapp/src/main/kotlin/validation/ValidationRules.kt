package kickstart.validation

import kickstart.validation.ValidationResult.Failure
import kickstart.validation.ValidationResult.Success


val failure: ValidationRule<String?, String> = { key ->
    object : Validator<String?, String> {
        override val key = key

        override fun invoke(value: String?) = Failure<String>(Violation(key, listOfNotNull(value)))
    }
}

val notBlank: ValidationRule<String?, String> = { key ->
    object : Validator<String?, String> {
        override val key = key

        override fun invoke(value: String?): ValidationResult<String> =
            if (value.isNullOrBlank()) Failure(Violation(key)) else Success(value)
    }
}
