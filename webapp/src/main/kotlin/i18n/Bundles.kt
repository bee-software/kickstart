package kickstart.i18n

import java.nio.file.Path
import java.nio.file.Paths
import java.text.MessageFormat
import java.util.*

class MessageBundle(private val bundle: ResourceBundle) : Messages {

    override fun interpolate(key: String, vararg args: Any): String {
        val formatter = MessageFormat(bundle.getString(key), bundle.locale)
        return formatter.format(args)
    }

    companion object {
        fun load(bundleName: String, locale: Locale) = MessageBundle(ResourceBundle.getBundle(bundleName, locale))
    }
}


class BundledMessages(private val root: Path) {

    fun at(path: Path): BundledMessages = BundledMessages(resolve(path))

    fun load(path: Path, locale: Locale): Messages {
        val messages = loadBundle(resolve(path), locale) +
                loadBundle(resolve(path.resolveSibling("defaults")), locale)

        return path.parent?.let { messages + load(it, locale) } ?: messages
    }

    private fun loadBundle(path: Path, locale: Locale): Messages {
        return try {
            MessageBundle.load(path.toString(), locale)
        } catch (e: MissingResourceException) {
            notAvailable(path, locale)
        }
    }

    private fun resolve(path: Path) = root.resolve(path)

    companion object {
        fun rootedAt(path: String) = BundledMessages(Paths.get(path))
    }
}

fun BundledMessages.loadBundle(path: String, name: String, locale: Locale) =
    at(Paths.get(path)).load(Paths.get(name), locale)


private fun notAvailable(bundlePath: Path, locale: Locale): Messages {
    return Messages { message, _ ->
        throw MissingResourceException(
            "Can't find resource bundle for path $bundlePath and locale $locale",
            bundlePath.toString(),
            message
        )
    }
}
