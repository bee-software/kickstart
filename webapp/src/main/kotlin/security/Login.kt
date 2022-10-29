package kickstart.security

import kickstart.i18n.L10n
import kickstart.i18n.L10ned
import kickstart.i18n.I18ned
import kickstart.validation.*
import kickstart.validation.ValidationResult.Failure
import kickstart.validation.ValidationResult.Success

data class Login(
    val email: String? = null,
    private val violations: List<Violation> = listOf(),
    private val l10n: L10n = L10n()
) : L10ned by l10n, I18ned by l10n {

    fun errors() = ErrorMessages(prefix = "errors", violations = violations, lookup = l10n::lookup)

    operator fun plus(result: ValidationResult<*>) = when (result) {
        is Success -> this
        is Failure -> copy(violations = violations + result.violations)
    }

    companion object {
        val empty = Login()

        fun invalid(email: String?): Login = Login(email) + errors.login.credentials.invalid(email)
    }
}


object errors : ValidationKeys() {
    object login : ValidationKeys() {
        object credentials : ValidationKeys() {
            val invalid by failure
        }

        object email : ValidationKeys() {
            val required by notBlank
        }

        object password : ValidationKeys() {
            val required by notBlank
        }
    }
}
