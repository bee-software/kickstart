package kickstart.security

import kickstart.i18n.*
import kickstart.validation.*
import kickstart.validation.ValidationResult.Failure
import kickstart.validation.ValidationResult.Success

data class Login(
    val username: String? = null,
    private val violations: List<Violation> = listOf(),
    private val messages: LocalizedMessages = noMessages
) : I18ned {
    val lang by messages::language
    val t by messages::interpolation

    override fun localize(messages: LocalizedMessages) = copy(messages = messages)

    fun errors() = ErrorMessages(prefix = "errors", violations = violations, lookup = messages.lookup)

    operator fun plus(result: ValidationResult<*>) = when (result) {
        is Success -> this
        is Failure -> copy(violations = violations + result.violations)
    }

    companion object {
        val empty = Login()

        fun invalid(username: String?): Login = Login(username) + errors.login.credentials.invalid(username)
    }
}


object errors : ValidationKeys() {
    object login : ValidationKeys() {
        object credentials : ValidationKeys() {
            val invalid by failure
        }

        object username : ValidationKeys() {
            val required by notBlank
        }

        object password : ValidationKeys() {
            val required by notBlank
        }
    }
}
