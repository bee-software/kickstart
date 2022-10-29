package kickstart

import kickstart.i18n.I18ned
import kickstart.i18n.L10n
import kickstart.i18n.L10ned
import kickstart.security.Username
import kickstart.security.md5


data class Home(
    val username: Username? = null,
    private val l10n: L10n = L10n(),
) : L10ned by l10n, I18ned by l10n{

    val gravatar get() = username.toString().md5()
}
