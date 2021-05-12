package kickstart.i18n

import java.text.MessageFormat
import java.util.*

class MessageBundle(private val bundle: ResourceBundle) : Messages {

    override fun interpolate(key: String, vararg args: Any): String {
        val formatter = MessageFormat(bundle.getString(key), bundle.locale)
        return formatter.format(args)
    }

    companion object {
        fun load(bundleName: String, locale: Locale)= MessageBundle(ResourceBundle.getBundle(bundleName, locale))
    }
}